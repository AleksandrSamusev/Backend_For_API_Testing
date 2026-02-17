# 📋 User API: Business Requirements & Validation Rules

This document serves as the official **Source of Truth** for the User Management API sandbox. It defines the constraints and expected behaviors used to design testing scenarios.

### 1. Name Validation (First Name & Last Name)
1.  **Mandatory:** Fields must not be null, empty, or contain only whitespaces.
2.  **Length:** Must be between **2 and 50 characters**.
3.  **Character Set:** Must only contain letters, hyphens, and spaces.
4.  **Security:** Must explicitly reject **SQL injection** patterns (e.g., `' DROP TABLE`) and **XSS tags** (e.g., `<script>`).
5.  **Data Integrity:** Numeric values and special characters (except hyphens/spaces) are prohibited.

### 2. Email Address Validation
1.  **Mandatory:** Must not be null or blank.
2.  **Uniqueness:** Emails must be unique; attempting to register an existing email returns a **409 Conflict**.
3.  **Format:** Must be a valid **RFC-compliant** address (requires `@` and a domain dot).
4.  **Normalization:** The system must trim leading/trailing spaces and handle mixed-case domains (e.g., `USER@Domain.Com`).
5.  **TLD Constraints:** The Top-Level Domain must be between **2 and 6 characters**.
6.  **Length:** Total email length must not exceed **254 characters**.
7.  **Features:** Must support sub-addressing (e.g., `name+tag@domain.com`).

### 3. Phone Number Validation
1.  **Mandatory:** Must not be blank.
2.  **Strict Format:** Must start with a `+` symbol followed exclusively by digits.
3.  **Length:** Must contain between **11 and 15 digits** (excluding the plus sign).
4.  **Constraints:** No alphabetic characters or internal formatting (spaces/dashes) are permitted.

### 4. Retrieval & Resource Handling (GET)
1.  **Individual Lookup:** Users are retrieved via a unique ID.
2.  **Non-Existing Resources:** Requesting an ID that does not exist returns a **404 Not Found**.
3.  **Type Safety:** The system expects IDs to be of type **Long**. Invalid formats (e.g., `abc-123`) return a **400 Bad Request**.
4.  **Bulk Retrieval:** The system must support listing all users with a guaranteed **200 OK** response.

### 5. Data Listing & Sorting
1.  **Default State:** Results are returned with `ID_ASC` sorting if no parameter is specified.
2.  **Supported Strategies:**
    *   `FIRST_NAME_ASC` / `FIRST_NAME_DESC`
    *   `LAST_NAME_ASC` / `LAST_NAME_DESC`
    *   `ID_ASC` / `ID_DESC`

### 6. Update & Delete Operations
1.  **Full Update:** Supports updating all user fields simultaneously, adhering to all validation rules above.
2.  **Safe Deletion:** Deleting an existing user returns a **200 OK**.
3.  **Format Validation:** Delete operations enforce the same **Long** ID type validation as GET requests.

### 7. Response Contract Standards
1.  **Success Response:** Every successful interaction must return a standard wrapper containing: `success`, `message`, `data` (object or list), and `timestamp`.
2.  **Error Response:** Every failed interaction must return: `success` (false), `errorCode`, `message`, an `errors` detail object, the request `path`, and a `timestamp`.