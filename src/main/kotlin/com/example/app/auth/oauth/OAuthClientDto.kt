package com.example.app.auth.oauth

import com.example.app.auth.entity.OAuthClient


data class OAuthClientDto(
  val id: Long,
  val name: String,
  val email: String
) {
  companion object {
    fun fromEntity(entity: OAuthClient): OAuthClientDto {
      return OAuthClientDto(
        id = entity.id!!, // TODO: Handle nullability properly
        name = entity.name,
        email = entity.email
      )
    }
  }
}
