package com.example.app.auth.jwt.component

import com.example.app.auth.jwt.model.dto.JwtPayloadDto
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import javax.crypto.SecretKey

@Component
class JwtParser(
  private val jwtKey: SecretKey,
  private val objectMapper: ObjectMapper
) {

  /**
   * Parses the JWT token and extracts the payload as a [JwtPayloadDto].
   *
   * @throws ExpiredJwtException if the token is expired.
   * @param token The JWT token to parse.
   * @return The parsed payload as a [JwtPayloadDto].
   */
  fun getPayload(token: String): JwtPayloadDto {
    val claims = Jwts.parser()
      .verifyWith(jwtKey)
      .build()
      .parseSignedClaims(token)
      .payload

    return objectMapper.convertValue(claims, JwtPayloadDto::class.java)
  }
}