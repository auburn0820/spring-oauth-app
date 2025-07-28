package com.example.spring_oauth_app.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.Date

class JwtUtilTest : BehaviorSpec({
  given("JWTUtil 인스턴스가 있을 때") {
    val jwtProperties = JwtProperties(
      secret = "mySecretKey123456789012345678901234567890",
      accessTokenExpirationMs = 100, // 0.1초
      refreshTokenExpirationMs = 86400000 // 1 day
    )

    val jwtUtil = JwtUtil(jwtProperties = jwtProperties)
    val userId: Long = 1234567890123456789L
    val username = "testuser"
    val email = "abc@google.com"

    `when`("액세스 토큰을 생성할 때") {
      val token = jwtUtil.generateToken(id = userId, name = username, email = email, tokenType = TokenType.ACCESS)
      then("토큰이 null이 아니어야 한다") {
        token shouldNotBe null
        token shouldNotBe ""
      }
    }

    `when`("리프레시 토큰을 생성할 때") {
      val token = jwtUtil.generateToken(id = userId, name = username, email = email, tokenType = TokenType.REFRESH)
      then("토큰이 null이 아니어야 한다") {
        token shouldNotBe null
        token shouldNotBe ""
      }
    }

    `when`("id가 String 타입으로 들어간 토큰을 파싱하면") {
      val token = Jwts.builder()
        .claim("id", userId.toString())
        .claim("name", username)
        .claim("email", email)
        .claim("token_type", TokenType.ACCESS.value)
        .issuedAt(Date(System.currentTimeMillis()))
        .expiration(Date(System.currentTimeMillis() + 10000))
        .signWith(Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray()))
        .compact()
      then("id가 정상적으로 Long으로 파싱된다") {
        val payload = jwtUtil.getPayload(token)
        payload.id shouldBe userId
      }
    }

    `when`("id가 Number 타입으로 들어간 토큰을 파싱하면") {
      val token = Jwts.builder()
        .claim("id", userId)
        .claim("name", username)
        .claim("email", email)
        .claim("token_type", TokenType.ACCESS.value)
        .issuedAt(Date(System.currentTimeMillis()))
        .expiration(Date(System.currentTimeMillis() + 10000))
        .signWith(Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray()))
        .compact()
      then("id가 정상적으로 Long으로 파싱된다") {
        val payload = jwtUtil.getPayload(token)
        payload.id shouldBe userId
      }
    }

    `when`("만료된 토큰을 getPayload로 파싱하면") {
      val token = Jwts.builder()
        .claim("id", userId)
        .claim("name", username)
        .claim("email", email)
        .claim("token_type", TokenType.ACCESS.value)
        .issuedAt(Date(System.currentTimeMillis() - 10000))
        .expiration(Date(System.currentTimeMillis() - 5000))
        .signWith(Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray()))
        .compact()
      then("예외가 발생한다") {
        shouldThrowAny {
          jwtUtil.getPayload(token)
        }
      }
    }

    `when`("만료된 토큰을 isExpired로 확인하면") {
      val token = Jwts.builder()
        .claim("id", userId)
        .claim("name", username)
        .claim("email", email)
        .claim("token_type", TokenType.ACCESS.value)
        .issuedAt(Date(System.currentTimeMillis() - 10000))
        .expiration(Date(System.currentTimeMillis() - 5000))
        .signWith(Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray()))
        .compact()
      then("true를 반환한다") {
        jwtUtil.isExpired(token) shouldBe true
      }
    }

    `when`("유효한 토큰을 isExpired로 확인하면") {
      val token = Jwts.builder()
        .claim("id", userId)
        .claim("name", username)
        .claim("email", email)
        .claim("token_type", TokenType.ACCESS.value)
        .issuedAt(Date(System.currentTimeMillis() + 1000))
        .expiration(Date(System.currentTimeMillis() + 5000))
        .signWith(Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray()))
        .compact()
      then("false를 반환한다") {
        jwtUtil.isExpired(token) shouldBe false
      }
    }
  }
})
