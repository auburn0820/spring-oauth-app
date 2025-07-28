package com.example.app.auth.jwt.component

import com.example.app.auth.jwt.model.JwtProperties
import com.example.app.auth.jwt.model.TokenType
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtGenerator(
  private val jwtKey: SecretKey,
  private val jwtProperties: JwtProperties
) {

  fun generateToken(name: String, email: String, id: Long, type: TokenType): String = Jwts.builder()
    .claim("id", id.toString())
    .claim("name", name)
    .claim("email", email)
    .claim("type", type)
    .issuedAt(Date(System.currentTimeMillis()))
    .expiration(Date(System.currentTimeMillis() + getExpiredMs(type)))
    .signWith(jwtKey)
    .compact()

  private fun getExpiredMs(type: TokenType): Long = when (type) {
    TokenType.ACCESS -> jwtProperties.accessTokenExpirationMs
    TokenType.REFRESH -> jwtProperties.refreshTokenExpirationMs
  }.toMillis()
}