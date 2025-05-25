# DemoSecurity Project

## Overview
This project is a simple Spring Boot application that implements user authentication and authorization using **JWT (JSON Web Tokens)**. 
It includes features such as user registration, login, and secure access to protected resources. 
The project uses **Spring Security** for authentication and authorization, and **MySQL** for database.

## Features
- User registration with password encryption using **BCrypt**.
- User login with JWT token generation.
- JWT-based authentication for secure API access.
- Custom JWT filter for token validation.


## Technologies Used
- **Java** (JDK 17+)
- **Spring Boot** (3.x)
- **Spring Security**
- **JWT (io.jsonwebtoken)**
- **Maven** (Dependency Management)
- **MySQL** (Database)

## Prerequisites
- **Java 17** or higher installed.
- **Maven** installed.
- **MySQL** database running (if using database-backed user management).

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd DemoSecurity
```

### 2. Configure the Database
Update the `application.properties` file in the `src/main/resources` directory with your MySQL database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 3. Add Dependencies
Ensure the following dependencies are included in your `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

### 4. Build the Project
Run the following command to build the project and download dependencies:
```bash
mvn clean install
```

### 5. Run the Application
Start the application using:
```bash
mvn spring-boot:run
```

### 6. Test the Endpoints
Use tools like **Postman** or **cURL** to test the following endpoints:

#### Public Endpoints
- **Register**: `POST /register`
- **Login**: `POST /login`

#### Protected Endpoints
- Access any other endpoint with a valid JWT token in the `Authorization` header (e.g., `Bearer <token>`).

## Security Configuration
The security configuration is defined in `src/main/java/com/example/DemoSecurity/config/WebSecurityConfig.java`. It includes:
- Disabling CSRF for simplicity.
- Permitting access to `/register` and `/login` endpoints.
- Securing all other endpoints with JWT authentication.

## JWT Configuration
The JWT service is implemented in `src/main/java/com/example/DemoSecurity/service/JwtService.java`. It includes:
- Token generation with claims, issuer, and expiration.
- Token validation and username extraction.

## License
This project is licensed under the MIT License.
