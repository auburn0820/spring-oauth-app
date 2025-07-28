package com.example.app.auth.common.configuration

import com.example.app.auth.jwt.model.JwtProperties
import io.jsonwebtoken.Jwts
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class JwtConfiguration(
  private val jwtProperties: JwtProperties
) {

  @Bean
  fun jwtKey(): SecretKey =
    SecretKeySpec(jwtProperties.secret.toByteArray(Charsets.UTF_8), Jwts.SIG.HS256.key().build().algorithm)
}