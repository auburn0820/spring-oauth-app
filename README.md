# Spring OAuth App

Spring Boot 기반 Google, Apple OAuth2 로그인 및 JWT 인증 예제 프로젝트입니다.

## 주요 기능
- Google, Apple OAuth2 로그인 지원
- JWT 기반 Access/Refresh Token 인증
- AccessToken 만료 시 RefreshToken으로 자동 재발급
- JWT 페이로드를 DTO로 안전하게 변환하여 사용
- H2 인메모리 DB, Swagger(OpenAPI) UI 제공
- User 엔티티 PK로 UUIDv7 자동 할당
- AuditingEntityListener로 created_at, updated_at 자동 관리

## 실행 방법
1. 환경 변수 설정 (Google/Apple OAuth Client ID, Secret, JWT Secret 등)
2. Gradle 빌드 및 실행
   ```bash
   ./gradlew bootRun
   ```
3. Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
4. H2 콘솔: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

## 환경 변수 예시
- GOOGLE_CLIENT_ID
- GOOGLE_CLIENT_SECRET
- APPLE_CLIENT_ID
- APPLE_CLIENT_SECRET
- JWT_SECRET

## 주요 경로 및 엔드포인트
- `/` : 홈(로그인 후 진입)
- `/login` : 소셜 로그인 페이지 (Spring Security 기본 제공)
- `/login/oauth2/code/google` : Google OAuth2 리다이렉트 URI
- `/login/oauth2/code/apple` : Apple OAuth2 리다이렉트 URI
- `/swagger-ui/index.html` : API 문서
- `/h2-console` : H2 DB 콘솔

## 인증 및 토큰 관리
- AccessToken 만료 시 RefreshToken을 확인, 만료되지 않았으면 AccessToken 재발급
- RefreshToken도 만료되었으면 401 Unauthorized로 로그인 재요구
- JWT 파싱 시 예외(만료, 변조 등)는 만료로 간주
- JWT 페이로드를 DTO로 변환하여 안전하게 사용

## 폴더 구조
```
src/main/kotlin/com/example/app/auth/
  ├── controller/   # 컨트롤러 (HomeController 등)
  ├── entity/       # 엔티티 (OAuthClient 등)
  ├── exception/    # 예외 처리
  ├── jwt/          # JWT 유틸, 필터, 인증/발급
  ├── oauth/        # OAuth2 관련 서비스, DTO, SuccessHandler 등
  ├── repository/   # JPA 리포지토리
  ├── service/      # 서비스 계층
  ├── common/       # 공통 유틸
src/main/resources/
  ├── application.yaml  # 환경설정 및 OAuth2 클라이언트/프로바이더 설정
```

## OAuth2 클라이언트/프로바이더 설정 예시 (application.yaml)
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            redirect-uri: http://localhost:8080/login/oauth2/code/google
            ...
          apple:
            client-id: ${APPLE_CLIENT_ID}
            client-secret: ${APPLE_CLIENT_SECRET}
            redirect-uri: http://localhost:8080/login/oauth2/code/apple
            ...
        provider:
          google:
            authorization-uri: https://accounts.google.com/o/oauth2/v2/auth
            token-uri: https://oauth2.googleapis.com/token
            user-info-uri: https://openidconnect.googleapis.com/v1/userinfo
            ...
```

## 참고
- Spring Security OAuth2 공식 문서: https://docs.spring.io/spring-security/reference/servlet/oauth2/
- JWT 공식 문서: https://jwt.io/
