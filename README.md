# User Authentication System (Spring Boot)

A production-grade authentication system built using Spring Boot, JWT, Redis, and MySQL.

## 🚀 Features
- User registration & login
- JWT access tokens
- Refresh tokens stored in MySQL
- Stateless logout using Redis blacklist
- Role-based authorization
- Secure password hashing (BCrypt)
- Environment-based configuration

## 🏗 Architecture
- Controller → Service → Repository (layered)
- DTOs for request/response
- JPA entities for persistence
- Spring Security filter chain
- Redis for token revocation

## 🔐 Authentication Flow
1. User logs in with email & password
2. Access token (short-lived) is issued
3. Refresh token (long-lived) is stored in DB
4. Logout → token added to Redis blacklist
5. Blacklisted tokens are rejected

## 🛠 Tech Stack
- Java (LTS)
- Spring Boot 4
- Spring Security 6
- MySQL
- Redis (Upstash)
- JPA / Hibernate
- JWT (JJWT)

## ⚙️ Configuration
All secrets are injected via environment variables.
`application-local.properties` is excluded from version control.

## 📌 Future Enhancements
- Refresh token rotation
- OAuth2 login
- Rate limiting
- Audit logging
