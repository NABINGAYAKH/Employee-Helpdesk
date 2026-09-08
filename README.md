# AI-Powered Employee IT Helpdesk

A backend REST API for managing employee IT support tickets, authentication, support agents, ticket workflows, and AI-assisted ticket analysis.

The system is designed around a helpdesk workflow where employees can create support tickets, support agents can manage them, and AI-assisted analysis can provide a category, priority, summary, and suggested solution for human review.

## Features

* Employee management with CRUD operations
* Support agent management
* IT ticket creation and management
* Ticket status lifecycle:

    * `OPEN`
    * `IN_PROGRESS`
    * `RESOLVED`
    * `CLOSED`
* Ticket priority levels:

    * `LOW`
    * `MEDIUM`
    * `HIGH`
    * `CRITICAL`
* Ticket filtering by status and priority
* JWT-based authentication
* BCrypt password hashing
* Role-based authorization
* Three user roles:

    * `EMPLOYEE`
    * `SUPPORT_AGENT`
    * `ADMIN`
* Employee ticket ownership validation
* Global exception handling
* Jakarta Bean Validation
* AI-assisted ticket analysis
* AI recommendation approval/rejection workflow
* Persistent AI analysis status using PostgreSQL

## AI Ticket Analysis

The project currently uses a **mock AI analysis service** to demonstrate the AI-assisted helpdesk workflow without requiring a paid LLM API.

The analysis examines ticket information and produces:

* Category
* Recommended priority
* Summary
* Suggested solution
* Analysis status

Example categories include:

* `NETWORK`
* `ACCESS`
* `HARDWARE`
* `APP`
* `OTHER`

### Human-in-the-loop workflow

AI recommendations are not automatically applied to the ticket.

The workflow is:

```text
Employee creates ticket
        ↓
AI analyzes ticket
        ↓
AI recommendation stored as PENDING
        ↓
Support Agent / Admin reviews recommendation
        ↓
     ┌───────────────┐
     ↓               ↓
  APPROVE          REJECT
     ↓               ↓
Apply recommendation  Keep ticket unchanged
```

This demonstrates a human-in-the-loop approach where support personnel remain responsible for accepting or rejecting AI recommendations.

## Authentication & Authorization

The application uses Spring Security with JWT authentication.

### Authentication flow

```text
Register
   ↓
Password hashed using BCrypt
   ↓
User stored in PostgreSQL
   ↓
Login
   ↓
Authentication
   ↓
JWT generated
   ↓
Client sends JWT with requests
   ↓
JwtAuthFilter validates token
   ↓
Request authorized based on role
```

### Role-based access

| Role          | Purpose                                              |
| ------------- | ---------------------------------------------------- |
| EMPLOYEE      | Create and access permitted employee-level resources |
| SUPPORT_AGENT | Manage support tickets and review AI recommendations |
| ADMIN         | Administrative operations and elevated access        |

Protected endpoints are secured using Spring Security role-based authorization.

## Technology Stack

### Backend

* Java 21
* Spring Boot
* Spring Web MVC
* Spring Data JPA
* Hibernate
* Spring Security
* JWT
* Spring Validation
* Lombok

### Database

* PostgreSQL

### Development & Testing

* Maven
* IntelliJ IDEA
* Postman
* Git / GitHub

## Project Architecture

The application follows a layered backend architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
JPA / Hibernate
    ↓
PostgreSQL
```

Security is handled through:

```text
Client
   ↓
JWT
   ↓
JwtAuthFilter
   ↓
Spring Security
   ↓
Role-Based Authorization
   ↓
Controller
```

### Main package structure

```text
com.nabin.employee_helpdesk
│
├── config
│   ├── JwtAuthFilter
│   └── SecurityConfig
│
├── controller
│   ├── AiController
│   ├── AuthController
│   ├── EmployeeController
│   ├── SupportAgentController
│   └── TicketController
│
├── dto
│
├── entity
│
├── exception
│
├── repository
│
└── service
```

## API Endpoints

### Authentication

```http
POST /api/auth/register
POST /api/auth/login
```

### Employees

```http
POST   /api/employees
GET    /api/employees
GET    /api/employees/{id}
PUT    /api/employees/{id}
DELETE /api/employees/{id}
```

### Support Agents

```http
POST   /api/support-agents
GET    /api/support-agents
GET    /api/support-agents/{id}
PUT    /api/support-agents/{id}
DELETE /api/support-agents/{id}
```

### Tickets

```http
POST   /api/tickets
GET    /api/tickets
GET    /api/tickets/{id}
PUT    /api/tickets/{id}
DELETE /api/tickets/{id}
```

Tickets can also be filtered using status and priority.

### AI

```http
POST /api/ai/analyze/{ticketId}
POST /api/ai/approve/{ticketId}
POST /api/ai/reject/{ticketId}
```

## Database Configuration

The application uses PostgreSQL.

Create a database named:

```sql
CREATE DATABASE employee_helpdesk;
```

Database credentials are loaded through environment variables rather than being stored directly in the source code.

Example configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/employee_helpdesk
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}
```

## Environment Variables

Before running the application, configure:

```text
DB_PASSWORD=<your-postgresql-password>
JWT_SECRET=<your-jwt-secret>
```

Do not commit real passwords, API keys, or JWT secrets to GitHub.

## Running the Application

### 1. Clone the repository

```bash
git clone https://github.com/NABINGAYAKH/employee-helpdesk.git
```

### 2. Open the project

Open the project in IntelliJ IDEA or another Java IDE.

### 3. Configure environment variables

Set:

```text
DB_PASSWORD
JWT_SECRET
```

### 4. Start PostgreSQL

Make sure PostgreSQL is running and the `employee_helpdesk` database exists.

### 5. Run the application

Using Maven:

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8081
```

## Testing

The REST APIs can be tested using Postman.

The project includes Spring Boot test infrastructure and can be extended with unit and integration tests for services, security, ticket workflows, and AI analysis.

## Future Improvements

Planned improvements include:

* Comprehensive automated testing
* Swagger / OpenAPI documentation
* Docker containerization
* Cloud deployment
* Integration with a production LLM provider
* Improved AI ticket classification and recommendation
* Additional monitoring and production-readiness improvements

## Project Status

The core backend functionality is implemented, including:

* REST APIs
* PostgreSQL persistence
* Authentication
* JWT security
* Role-based authorization
* Ticket lifecycle management
* Ticket ownership validation
* AI-assisted ticket analysis
* Human approval/rejection workflow

The project is being further enhanced with testing, API documentation, containerization, and deployment.

## Author

**Nabin Gayak H**

Java Backend Developer | Spring Boot

GitHub: `https://github.com/NABINGAYAKH`
