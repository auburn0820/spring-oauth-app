package com.example.app.auth.oauth

import com.example.app.auth.jwt.component.JwtGenerator
import com.example.app.auth.jwt.model.TokenType
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KLogging
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2LoginSuccessHandler(
  private val jwtGenerator: JwtGenerator
) : AuthenticationSuccessHandler {
  companion object : KLogging()

  override fun onAuthenticationSuccess(
    request: HttpServletRequest,
    response: HttpServletResponse,
    authentication: Authentication
  ) {
    val oAuth2User = authentication.principal as CustomOAuth2User

    val name = oAuth2User.name
    val email = oAuth2User.email
    val id = oAuth2User.id

    val accessToken = jwtGenerator.generateToken(
      name = name,
      email = email,
      id = id,
      type = TokenType.ACCESS
    )

    val refreshToken = jwtGenerator.generateToken(
      name = name,
      email = email,
      id = id,
      type = TokenType.REFRESH
    )

    addCookie(response = response, name = TokenType.ACCESS.cookieKey, value = accessToken)
    addCookie(response = response, name = TokenType.REFRESH.cookieKey, value = refreshToken)
    response.sendRedirect("http://localhost:8080/")
  }

  private fun addCookie(
    response: HttpServletResponse,
    maxAge: Int = 60 * 60 * 24, // 1 day
    name: String,
    value: String
  ) {
    val cookie = Cookie(name, value).apply {
      path = "/"
      isHttpOnly = true
      this.maxAge = maxAge
    }
    response.addCookie(cookie)
  }
}