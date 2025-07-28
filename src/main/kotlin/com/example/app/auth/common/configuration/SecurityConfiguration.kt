package com.example.app.auth.common.configuration

import com.example.app.auth.jwt.JwtFilter
import com.example.app.auth.oauth.OAuth2LoginSuccessHandler
import com.example.app.auth.service.CustomOAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
  private val customOAuth2UserService: CustomOAuth2UserService,
  private val oAuth2LoginSuccessHandler: OAuth2LoginSuccessHandler,
  private val jwtFilter: JwtFilter
) {

  companion object {
    val publicUrls = listOf(
      "/error",
      "h2-console/**",
      "/",
      "/swagger-ui/**",
      "/v3/api-docs/**",
      "/swagger-resources/**",
      "/actuator/**",
      "/swagger-ui.html" // 호환을 위해 Swagger 모두 필요.
    )
  }

  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
    http.csrf { it.disable() }
      .formLogin { it.disable() }
      .httpBasic { it.disable() }
      .authorizeHttpRequests {
        it.requestMatchers(
          *publicUrls.toTypedArray()  // vararg로 변환
        ).permitAll()

        it.anyRequest()
          .authenticated()
      }
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
      .oauth2Login { customizer ->
        customizer.userInfoEndpoint { it.userService(customOAuth2UserService) }
          .successHandler(oAuth2LoginSuccessHandler)
      }
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .headers { it.frameOptions { fo -> fo.sameOrigin() } }
      .build()
}