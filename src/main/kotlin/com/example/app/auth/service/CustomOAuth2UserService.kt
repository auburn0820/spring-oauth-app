package com.example.app.auth.service

import com.example.app.auth.entity.OAuthClient
import com.example.app.auth.exception.UnsupportedRegistrationIdException
import com.example.app.auth.oauth.CustomOAuth2User
import com.example.app.auth.oauth.OAuthClientDto
import com.example.app.auth.repository.OAuthClientRepository
import mu.KLogging
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
  private val oAuthClientRepository: OAuthClientRepository
) : DefaultOAuth2UserService() {

  companion object : KLogging()

  override fun loadUser(userRequest: OAuth2UserRequest): CustomOAuth2User {
    val oAuth2User = super.loadUser(userRequest)
    val attributes = oAuth2User.attributes
    val registrationId = userRequest.clientRegistration.registrationId
    val email = when (registrationId) {
      "google" -> attributes["email"] as String
      "apple" -> attributes["email"] as String
      else -> throw UnsupportedRegistrationIdException(message = "Unsupported registration ID: $registrationId")
    }
    val name = attributes["name"]?.toString() ?: attributes["sub"]?.toString() ?: ""
    val oAuthClient = oAuthClientRepository.findByEmail(email = email) ?: OAuthClient(
      name = name,
      email = email
    ).also(oAuthClientRepository::save)

    return CustomOAuth2User(oAuthClientDto = OAuthClientDto.fromEntity(entity = oAuthClient))
  }
}