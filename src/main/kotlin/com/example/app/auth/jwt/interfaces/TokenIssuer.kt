package com.example.app.auth.jwt.interfaces

import com.example.app.auth.jwt.model.TokenType

interface TokenIssuer {
  fun issue(name: String, email: String, id: Long, tokenType: TokenType): String
}