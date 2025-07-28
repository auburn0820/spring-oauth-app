package com.example.app.auth.jwt.interfaces

import jakarta.servlet.http.HttpServletRequest

interface TokenCookieExtractor {
  fun extract(request: HttpServletRequest, name: String): String?
}