package com.example.app.auth.jwt.model.dto

import java.util.Date

data class JwtPayloadDto(
  val id: Long,
  val name: String,
  val email: String,
  val issuedAt: Long,
  val expiresAt: Long
) {
  fun issuedAtDate(): Date = Date(issuedAt)
  fun expiresAtDate(): Date = Date(expiresAt)
}