# SmartBank — Enterprise Banking & Fraud Detection Platform

SmartBank is a backend banking application built using Java and Spring Boot. It provides secure banking operations, JWT-based authentication, transaction management, and rule-based fraud detection.

## 🚀 Features

- User registration and login
- JWT authentication and authorization
- Secure password hashing using BCrypt
- Account management
- Deposits and withdrawals
- Money transfers between accounts
- Transaction history and pagination
- Rule-based fraud detection
- Fraud risk scoring and alerts
- Kafka-based transaction event publishing
- REST API documentation using Swagger

## 🛠️ Technology Stack

### Backend
- Java 26
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- Maven

### Database
- PostgreSQL

### Messaging
- Apache Kafka
- Event-driven architecture

### Security
- JWT
- BCrypt

### DevOps and Tools
- Docker
- Docker Compose
- JUnit
- Mockito
- Postman
- Swagger / OpenAPI
- Git and GitHub

## 🏗️ Architecture

The application follows a layered architecture:

- Controller Layer — handles HTTP requests.
- Service Layer — implements banking business logic.
- Repository Layer — communicates with PostgreSQL.
- Security Layer — handles authentication and authorization.
- Kafka Integration — publishes transaction events.

## 🔍 Fraud Detection

SmartBank uses rule-based fraud detection to identify suspicious transactions.

Example rules:
- Same sender and receiver: Critical risk.
- Transaction amount above ₹100,000: High risk.
- Normal transaction: Low risk.

Risk scores are generated and fraud alerts are created when applicable.

## ⚙️ Getting Started

### Prerequisites
- Java 26
- Docker Desktop
- Git

### Clone the repository

```bash
git clone https://github.com/saniikakatkhade/smartbank.git
cd smartbank
```

### Configure environment variables

Set your database password and JWT secret securely through environment variables. Never commit actual credentials.

### Build the application

```bash
.\mvnw.cmd clean verify
```

### Run using Docker Compose

```bash
docker compose up -d --build
```

## 📖 API Documentation

Once the application is running, access:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 🧪 Testing

The project includes JUnit and Mockito tests. API functionality can also be tested using Postman.

## 👩‍💻 Developer

**Sanika Katkhade**

GitHub: https://github.com/saniikakatkhade

---

*SmartBank — Secure Banking with Intelligent Fraud Detection.*
