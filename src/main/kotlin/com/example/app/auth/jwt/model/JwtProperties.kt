package com.example.app.auth.jwt.model

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
  val secret: String,
  val accessTokenExpirationMs: Duration,
  val refreshTokenExpirationMs: Duration
)