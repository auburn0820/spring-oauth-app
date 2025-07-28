package com.example.app.auth.jwt.component

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import mu.KLogging
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtValidator(
  private val jwtKey: SecretKey
) {

  companion object : KLogging()

  fun isExpired(token: String): Boolean = try {
    val expiration = Jwts
      .parser()
      .verifyWith(jwtKey)
      .build()
      .parseSignedClaims(token)
      .payload
      .expiration

    expiration.before(Date(System.currentTimeMillis()))
  } catch (e: ExpiredJwtException) {
    logger.info(e) { "JWT token is expired: $token" }
    true
  }
}