# 🛠️ API Testing Sandbox: User Management

[![Java Version](https://img.shields.io/badge/Java-21+-orange?logo=java)](https://www.oracle.com)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?logo=springboot)](https://spring.io)
[![Swagger](https://img.shields.io/badge/Swagger-API_Docs-85EA2D?logo=swagger)](https://ratty-merrily-devpractice-8e62228c.koyeb.app/swagger-ui/index.html)
> **A specialized REST API playground** built as a stable target for both **manual and automated API testing practice**. While the architecture follows professional standards, the project’s heart is to serve as a "live basis" for testers to hone their craft.

## 🚀 How you could use this project

There are several ways to engage with this sandbox depending on your goals:

1.  **Local Development:** [Clone the repository](https://docs.github.com) and run the service locally for full control and real-time debugging.
2.  **Custom Deployment:** Take the source code and deploy your own instance to cloud platforms like [Koyeb](https://www.koyeb.com), [Render](https://render.com), or [Heroku](https://www.heroku.com).
3.  **Instant Practice:** Skip the setup and use the live version already hosted on **Koyeb** to start testing immediately against a remote environment.
    *   **Base URL:** `https://ratty-merrily-devpractice-8e62228c.koyeb.app`
> **Pro-tip:** Don't be afraid to break something! That's exactly why this exists—if you manage to crash it, we'll just fix it together. ))
---

## 📑 Table of Contents
1. [Core Features](#-core-features)
2. [Project Structure](#-project-structure)
3. [Data Persistence](#-data-persistence)
4. [API Architecture](#-api-architecture)
5. [Live Documentation](#-live-documentation)
6. [Endpoint Overview](#-endpoint-overview)
7. [Setup & Installation](#-setup--installation)

---

## 🚀 Core Features

*   **Standardized API Wrapper**: All responses follow the ApiResponse pattern for predictable frontend consumption.
*   **Domain Validation**: Custom logic for @ValidPhoneNumber, @ValidEmail, and @ValidName.
*   **Dynamic Sorting**: Integrated SortingOptions enum support for list queries.
*   **Partial Updates**: Support for DTO-based profile modifications via PUT.

---

## 📂 Project Structure

The project follows a standard **Spring Boot N-Tier Architecture**, ensuring a clean separation between the web layer, business logic, and data access.

```text
.
├── src/main/java/dev/practice/shopapp/
│   ├── controllers/          # REST Controllers (Entry points)
│   ├── dto/                  # Data Transfer Objects
│   ├── exceptions/           # Custom exceptions & Global Error Handling
│   ├── mappers/              # Entity-DTO mapping logic
│   ├── models/               # Data Entities (Domain Model)
│   ├── repositories/         # Data Access Layer
│   ├── services/             # Business logic implementation
│   ├── utils/                # General utility classes
│   ├── validation/           # Custom Jakarta Constraints & Validators
│   ├── ShopappApplication.java # Main Entry Point
│   └── SortingOptions.java   # Custom Enum for Query Sorting
└── users.txt                 # Flat-file data storage for simplified testing

```

## 💾 Data Persistence

To facilitate rapid **API testing and development**, this application utilizes a **lightweight flat-file persistence strategy**.

*   **Implementation:** Data is stored and managed within a `users.txt` file in the root directory.
*   **Strategic Advantage:** By opting for a file-based system over a traditional RDBMS (like PostgreSQL), the project eliminates environment-specific setup hurdles, making it an ideal "plug-and-play" sandbox for practicing RESTful API interactions.

> **Why the .txt file?** To be honest, using CSV format in a text file means you don't have to worry about the "Pro" plan or wrestling with SQL persistence. We're keeping it free and simple so you can focus on the tests, not the database configuration! )))

---

## 🏗 API Architecture

### Standardized `ApiResponse` Wrapper

To ensure a contract-first approach for frontend integration, all endpoints utilize a central `ResponseUtil` to wrap data in a consistent `ApiResponse<T>` structure. This guarantees that every request—whether successful or failed—provides a predictable set of metadata, including status flags, timestamps, and request paths.

#### ✅ Successful Response
Returned when the operation completes as expected. The `data` field contains the requested resource, and `errors` remains `null`.

```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": 1771332281802,
    "firstName": "Chicko-Lazy",
    "lastName": "Honda",
    "email": "asd1233@tets.test",
    "phoneNumber": "+17026201348"
  },
  "errors": null,
  "errorCode": 0,
  "timestamp": 1771332557383,
  "path": "/api/v1/users/1771332281802"
}
```
#### ❌ Failed Response (Validation Error)
Triggered by invalid input or business logic violations. The `data` field is `null`, and the `errors` array provides specific details for the client.

```json
{
    "success": false,
    "message": "Validation error",
    "data": null,
    "errors": [
        "First name must only contain letters, hyphens, and spaces",
        "Please provide a valid email address (e.g., name@domain.com)",
        "Last name must be between 2 and 50 characters",
        "Phone number must start with '+' followed by 11 to 15 digits"
    ],
    "errorCode": 400,
    "timestamp": 1771338432585,
    "path": "/api/v1/users"
}
```

---



## 📖 Live Documentation

The project is fully self-documenting using Springdoc-OpenAPI. You can explore the endpoints, view data schemas, and execute live requests via the interactive UI.

> [!IMPORTANT]
> **Access the Live Swagger UI here:**  
> 👉 [**LIVE Swagger UI**](https://ratty-merrily-devpractice-8e62228c.koyeb.app/swagger-ui/index.html)

### Local Documentation
If running the application locally:
*   Interactive UI: http://localhost:8080/swagger-ui.html
*   OpenAPI Spec (JSON): http://localhost:8080/v3/api-docs

---

## 🎯 Testing Focus

Since this project is a **QA Sandbox**, the implementation is guided by strict validation rules to provide meaningful testing scenarios.

*   **Requirements:** You can find the detailed functional requirements and validation logic here: [📋 Requirements.md](./REQUIREMENTS.md)
*   **Test Scenarios:** The project is designed to be validated against 40+ scenarios, covering:
    *   Positive path (Standard valid data)
    *   Boundary analysis (Min/Max lengths)
    *   Security (SQL injection & XSS attempts)
    *   Data Integrity (Formatting and normalization)
    *   Contract Testing (Schema validation)

---

## 🛤 Endpoint Overview

> **Note:** Currently, this project focuses exclusively on the **User** entity. This is an intentional design choice to prioritize the depth of **API automation testing** over architectural complexity. The primary objective is to demonstrate comprehensive test coverage and rigorous scenario mapping, ensuring maximum defect detection through high-density test cases.

| Method | Endpoint | Description | Parameters / Request Body |
| :--- | :--- | :--- | :--- |
| POST | `/api/v1/users` | Register a new user | **Body:** `UserCreateDTO` |
| GET | `/api/v1/users?sortBy={option}` | List all users with optional sorting | **Query:** `sortBy` (Default: `ID_ASC`) |
| GET | `/api/v1/users/{id}` | Get user by unique ID | **Path:** `id` |
| PUT | `/api/v1/users/{id}` | Update profile details | **Body:** `UserUpdateDTO` |
| DELETE | `/api/v1/users/{id}` | Remove user account | None |

### 🔍 Sorting Options
When calling `GET /api/v1/users`, you can apply the `sortBy` query parameter using the following values:

* **Name-based:** `FIRST_NAME_ASC`, `FIRST_NAME_DESC`, `LAST_NAME_ASC`, `LAST_NAME_DESC`
* **ID-based:** `ID_ASC`, `ID_DESC`
---


## ⚙️ Setup & Installation

### Prerequisites
- JDK: 21+
- Build Tool: Maven 3.8+

### Quick Start
1. Clone the repository: git clone https://github.com
2. Build the application: ./mvnw clean install
3. Run the server: ./mvnw spring-boot:run

---

## 🔒 License
This project is licensed under the MIT License - see the LICENSE file for details.