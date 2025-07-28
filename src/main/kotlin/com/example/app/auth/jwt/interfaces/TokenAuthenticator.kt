package com.example.app.auth.jwt.interfaces

interface TokenAuthenticator {
  fun authenticate(token: String)
}