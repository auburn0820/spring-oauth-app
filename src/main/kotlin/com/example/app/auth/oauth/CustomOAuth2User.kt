package com.example.app.auth.oauth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

data class CustomOAuth2User(
  private val oAuthClientDto: OAuthClientDto
) : OAuth2User {

  val id get() = oAuthClientDto.id
  val email get() = oAuthClientDto.email

  override fun getAttributes(): Map<String?, Any?>? {
    return emptyMap()
  }

  override fun getAuthorities(): Collection<GrantedAuthority?>? {
    val collection: MutableCollection<GrantedAuthority?> = ArrayList()

    collection.add(GrantedAuthority { "ROLE_USER" }) // TODO: Consider whether role-related data is needed

    return collection
  }

  override fun getName(): String {
    return oAuthClientDto.name
  }
}
