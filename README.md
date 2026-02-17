# 🛒 Backend For API Testing Practice

[![Java Version](https://img.shields.io)](https://www.oracle.com)
[![Spring Boot](https://img.shields.io)](https://spring.io)
[![Swagger](https://img.shields.io)](https://ratty-merrily-devpractice-8e62228c.koyeb.app)

> **A professional RESTful API** for user account management, featuring standardized response wrappers, custom Jakarta validations, and automated OpenAPI 3 documentation.

---

## 📑 Table of Contents
1. [Core Features](#-core-features)
2. [API Architecture](#-api-architecture)
3. [Live Documentation](#-live-documentation)
4. [Endpoint Overview](#-endpoint-overview)
5. [Setup & Installation](#-setup--installation)

---

## 🚀 Core Features

*   **Standardized API Wrapper**: All responses follow the ApiResponse pattern for predictable frontend consumption.
*   **Domain Validation**: Custom logic for @ValidPhoneNumber, @ValidEmail, and @ValidName.
*   **Dynamic Sorting**: Integrated SortingOptions enum support for list queries.
*   **Partial Updates**: Support for DTO-based profile modifications via PUT.

---

## 🏗 API Architecture

### Global Response Wrapper
The system wraps every response to ensure the frontend always knows the status, timestamp, and specific error messages if a failure occurs. Example structure:

{
"success": true,
"message": "User found",
"data": {
"id": 101,
"firstName": "John",
"lastName": "Doe",
"email": "john.doe@example.com",
"phoneNumber": "+123456789"
},
"errors": null,
"errorCode": 0,
"timestamp": 1708174320000,
"path": "/api/v1/users/101"
}

---

## 📖 Live Documentation

The project is fully self-documenting using Springdoc-OpenAPI. You can explore the endpoints, view data schemas, and execute live requests via the interactive UI.

> [!IMPORTANT]
> **Access the Live Swagger UI here:**  
> 👉 [**https://ratty-merrily-devpractice-8e62228c.koyeb.app**](https://ratty-merrily-devpractice-8e62228c.koyeb.app)

### Local Documentation
If running the application locally:
*   Interactive UI: http://localhost:8080/swagger-ui.html
*   OpenAPI Spec (JSON): http://localhost:8080/v3/api-docs

---

## 🛤 Endpoint Overview

| Method | Endpoint | Description | Request Body |
| :--- | :--- | :--- | :--- |
| POST | /api/v1/users | Register a new user | UserCreateDTO |
| GET | /api/v1/users | List all users (sorted) | None |
| GET | /api/v1/users/{id} | Get user by unique ID | None |
| PUT | /api/v1/users/{id} | Update profile details | UserUpdateDTO |
| DELETE | /api/v1/users/{id} | Remove user account | None |

---

## ⚙️ Setup & Installation

### Prerequisites
- JDK: 17+
- Build Tool: Maven 3.8+

### Quick Start
1. Clone the repository: git clone https://github.com
2. Build the application: ./mvnw clean install
3. Run the server: ./mvnw spring-boot:run

---

## 🔒 License
This project is licensed under the MIT License - see the LICENSE file for details.