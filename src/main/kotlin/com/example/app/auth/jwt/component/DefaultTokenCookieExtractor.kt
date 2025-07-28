package com.example.app.auth.jwt.component

import com.example.app.auth.jwt.interfaces.TokenCookieExtractor
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class DefaultTokenCookieExtractor : TokenCookieExtractor {
  override fun extract(request: HttpServletRequest, name: String): String? =
    request.cookies?.find { it.name == name }?.value
}