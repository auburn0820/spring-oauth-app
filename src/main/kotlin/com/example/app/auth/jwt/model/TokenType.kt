package com.example.app.auth.jwt.model

enum class TokenType(val value: String, val cookieKey: String) {
  ACCESS("access", "Authorization"),
  REFRESH("refresh", "TW-RefreshToken");
}