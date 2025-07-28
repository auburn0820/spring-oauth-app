package com.example.app.auth.jwt

import com.example.app.auth.jwt.model.TokenType
import com.example.app.auth.jwt.component.JwtGenerator
import com.example.app.auth.jwt.interfaces.TokenIssuer
import org.springframework.stereotype.Component

@Component
class JwtIssuer(
  private val jwtGenerator: JwtGenerator
) : TokenIssuer {
  override fun issue(
    name: String,
    email: String,
    id: Long,
    tokenType: TokenType
  ): String = jwtGenerator.generateToken(
    name = name,
    email = email,
    id = id,
    type = tokenType
  )
}