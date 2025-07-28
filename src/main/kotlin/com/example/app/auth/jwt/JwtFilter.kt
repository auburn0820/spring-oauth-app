package com.example.app.auth.jwt

import com.example.app.auth.common.ApiResponse
import com.example.app.auth.common.configuration.SecurityConfiguration
import com.example.app.auth.jwt.component.JwtParser
import com.example.app.auth.jwt.component.JwtValidator
import com.example.app.auth.jwt.model.TokenType
import com.fasterxml.jackson.databind.ObjectMapper
import com.example.app.auth.jwt.interfaces.TokenCookieExtractor
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
  private val cookieExtractor: TokenCookieExtractor,
  private val issuer: JwtIssuer,
  private val validator: JwtValidator,
  private val authenticator: JwtAuthenticator,
  private val parser: JwtParser,
  private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

  private val pathMatcher = AntPathMatcher()

  override fun shouldNotFilter(request: HttpServletRequest): Boolean {
    return SecurityConfiguration.publicUrls.any { path -> pathMatcher.match(path, request.requestURI) }
  }

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    val accessToken = cookieExtractor.extract(request, TokenType.ACCESS.cookieKey)
    val refreshToken = cookieExtractor.extract(request, TokenType.REFRESH.cookieKey)

    if (accessToken == null) {
      writeUnauthorizedResponse(response)
      return
    }

    if (validator.isExpired(accessToken)) {
      if (refreshToken == null || validator.isExpired(refreshToken)) {
        writeUnauthorizedResponse(response)
        return
      }

      val payload = parser.getPayload(refreshToken)
      val newAccessToken = issuer.issue(
        name = payload.name,
        email = payload.email,
        id = payload.id,
        tokenType = TokenType.ACCESS
      )

      response.addCookie(Cookie(TokenType.ACCESS.cookieKey, newAccessToken).apply {
        path = "/"
        isHttpOnly = true
        maxAge = 60 * 60 * 24
      })

      authenticator.authenticate(newAccessToken)
      filterChain.doFilter(request, response)
      return
    }

    authenticator.authenticate(accessToken)
    filterChain.doFilter(request, response)
  }

  private fun writeUnauthorizedResponse(response: HttpServletResponse) {
    response.status = HttpServletResponse.SC_UNAUTHORIZED
    response.contentType = MediaType.APPLICATION_JSON_VALUE
    response.characterEncoding = "UTF-8"
    val json = ApiResponse(code = 101, message = "Unauthorized", data = null)
    response.writer.write(objectMapper.writeValueAsString(json))
  }
}