# AI-Powered Employee IT Helpdesk

A backend REST API for managing employee IT support tickets, authentication, support agents, ticket workflows, and AI-assisted ticket analysis.

The system follows a helpdesk workflow where employees can create support tickets, support agents can manage them, and AI-assisted analysis provides ticket classification and recommendations for human review.

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
* Swagger / OpenAPI API documentation
* AI-assisted ticket analysis
* Human approval/rejection workflow for AI recommendations
* PostgreSQL persistence
* Automated tests
* Docker containerization
* Docker Compose
* Persistent PostgreSQL Docker volume
* Environment-based configuration

---

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

AI recommendations are not automatically applied to tickets.

```text
Employee creates ticket
        ↓
AI analyzes ticket
        ↓
Recommendation stored as PENDING
        ↓
Support Agent / Admin reviews recommendation
        ↓
     ┌───────────────┐
     ↓               ↓
  APPROVE          REJECT
     ↓               ↓
Apply recommendation  Keep ticket unchanged
```

This approach keeps a human responsible for accepting or rejecting an AI recommendation.

---

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
Spring Security checks roles
   ↓
Request authorized
```

### User roles

| Role            | Purpose                                              |
| --------------- | ---------------------------------------------------- |
| `EMPLOYEE`      | Employee-level operations and permitted resources    |
| `SUPPORT_AGENT` | Manage support tickets and review AI recommendations |
| `ADMIN`         | Administrative operations and elevated access        |

Protected endpoints are secured using Spring Security role-based authorization.

---

## Technology Stack

### Backend

* Java 21
* Spring Boot
* Spring Web MVC
* Spring Data JPA
* Hibernate
* Spring Security
* JWT
* Jakarta Validation
* Lombok

### Database

* PostgreSQL

### API Documentation

* Swagger
* OpenAPI

### Testing

* JUnit
* Mockito
* Spring Boot Test

### Development & Tools

* Maven
* IntelliJ IDEA
* Postman
* Git
* GitHub

### Containerization

* Docker
* Docker Compose
* Docker volumes
* Docker networks

---

## Project Architecture

The application follows a layered backend architecture:

```text
Client
   ↓
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

### Security architecture

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

### Docker architecture

```text
                    Docker Compose
                          │
             ┌────────────┴────────────┐
             ↓                         ↓
      Spring Boot                  PostgreSQL
      Container                   Container
      Port 8081                   Port 5432
             │                         │
             └──── Docker Network ─────┘
                                       │
                              Persistent Volume
```

---

## Project Structure

```text
com.nabin.employee_helpdesk
│
├── config
│   ├── JwtAuthFilter
│   ├── OpenApiConfig
│   └── SecurityConfig
│
├── controller
│   ├── AiController
│   ├── AuthController
│   ├── EmployeeController
│   ├── HelloController
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

---

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

Tickets can also be filtered by status and priority.

### AI

```http
POST /api/ai/analyze/{ticketId}
POST /api/ai/approve/{ticketId}
POST /api/ai/reject/{ticketId}
```

---

## Swagger / OpenAPI

Swagger UI is available when the application is running:

```text
http://localhost:8081/swagger-ui/index.html
```

OpenAPI documentation:

```text
http://localhost:8081/v3/api-docs
```

JWT authentication can be configured in Swagger using the **Authorize** button.

---

## Database Configuration

The application uses PostgreSQL.

For a local PostgreSQL installation, create:

```sql
CREATE DATABASE employee_helpdesk;
```

Database credentials and the JWT secret are provided through environment variables.

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/employee_helpdesk
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}
```

> When running through Docker Compose, the Spring Boot container connects to PostgreSQL using the Docker service name `postgres`.

---

## Environment Variables

Configure:

```text
DB_PASSWORD=<your-postgresql-password>
JWT_SECRET=<your-jwt-secret>
```

Do not commit real passwords, JWT secrets, API keys, or `.env` files to GitHub.

---

# Running Locally

### 1. Clone the repository

```bash
git clone https://github.com/NABINGAYAKH/Employee-Helpdesk.git
```

### 2. Configure PostgreSQL

Make sure PostgreSQL is running and the `employee_helpdesk` database exists.

### 3. Configure environment variables

Set:

```text
DB_PASSWORD
JWT_SECRET
```

### 4. Run the application

Using Maven:

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8081
```

---

# Running with Docker

The project includes a `Dockerfile` and `compose.yaml` for running the Spring Boot application and PostgreSQL together.

### Start the application

From the project root:

```bash
docker compose up -d --build
```

This starts:

```text
Spring Boot → localhost:8081
PostgreSQL  → localhost:5433
```

### Check containers

```bash
docker compose ps
```

### View application logs

```bash
docker compose logs employee-helpdesk
```

### Stop containers

```bash
docker compose down
```

The PostgreSQL data is stored in a persistent Docker volume, so stopping/restarting the containers does not remove the database data.

### Docker environment

Docker Compose provides:

* Spring Boot application container
* PostgreSQL container
* Docker network for container communication
* Persistent PostgreSQL volume
* Environment variable configuration

---

## Testing

The project includes automated tests covering application functionality, security, authentication, ticket workflows, AI functionality, and service/controller behavior.

Tests can be executed using the Maven wrapper:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

The current test suite contains **91 passing tests**.

---

## Future Improvements

* Integration with a production LLM provider
* More advanced AI ticket classification
* Improved AI-generated solutions
* Cloud deployment
* Production monitoring and observability
* CI/CD pipeline
* Additional integration testing

---

## Project Status

The core backend is implemented and containerized.

### Implemented

* REST APIs
* PostgreSQL persistence
* JPA / Hibernate
* JWT authentication
* BCrypt password hashing
* Role-based authorization
* Ticket lifecycle management
* Employee ticket ownership validation
* AI-assisted ticket analysis
* Human approval/rejection workflow
* Global exception handling
* Request validation
* Swagger / OpenAPI documentation
* Automated tests
* Dockerfile
* Docker Compose
* Persistent PostgreSQL volume
* Environment-based configuration
* Git/GitHub version control

### Current AI implementation

The AI functionality currently uses a mock analysis service to demonstrate the complete workflow without requiring a paid external LLM API.

A future version can integrate a production LLM provider.

---

## Author

**Nabin Gayak H**

Java Backend Developer | Spring Boot

GitHub: `https://github.com/NABINGAYAKH`
