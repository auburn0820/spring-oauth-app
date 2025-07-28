package com.example.app.auth.jwt


import com.example.app.auth.jwt.component.JwtParser
import com.example.app.auth.jwt.interfaces.TokenAuthenticator
import com.example.app.auth.oauth.CustomOAuth2User
import com.example.app.auth.oauth.OAuthClientDto
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class JwtAuthenticator(
  private val jwtParser: JwtParser
) : TokenAuthenticator {
  override fun authenticate(token: String) {
    val payload = jwtParser.getPayload(token)
    val clientDto = OAuthClientDto(
      id = payload.id,
      name = payload.name,
      email = payload.email
    )
    val customOAuth2User = CustomOAuth2User(oAuthClientDto = clientDto)
    val authenticationToken =
      UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.authorities)
    SecurityContextHolder.getContext().authentication = authenticationToken
  }
}