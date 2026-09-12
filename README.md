# Employee Management REST API

A RESTful API for managing employees, built with Java and Spring Boot. The project includes authentication, role-based authorization, validation, pagination, automated integration testing, and OpenAPI documentation.

## Live Demo

**[Swagger UI](https://employee-api-azxv.onrender.com/swagger-ui/index.html)**

> **Note:** The application is hosted on Render's free tier and may spin down after a period of inactivity. If the application has been inactive, it may take up to 2 minutes to start when first accessed.

## Features

- **CRUD Operations** — Create, retrieve, update, and delete employees
- **Input Validation** — Validate request data using Bean Validation
- **Global Exception Handling** — Consistent error responses across the API
- **Authentication** — JWT-based authentication with Spring Security
- **Role-Based Authorization** — Separate permissions for `ADMIN` and `EMPLOYEE` users
- **Pagination** — Paginated employee results
- **Integration Testing** — Automated tests using Testcontainers and PostgreSQL
- **API Documentation** — Interactive Swagger/OpenAPI documentation

## Demo Credentials

A demo admin account is provided so you can test all available endpoints.

**Username:** `admin@example.com`  
**Password:** `adminpassword`

The account has the `ADMIN` role and can perform all employee CRUD operations.

> **Note:** New registrations are assigned the `EMPLOYEE` role by default.

### Roles

- **EMPLOYEE** — Can view employee information
- **ADMIN** — Can view, create, update, and delete employee information

## Tech Stack

- **Java**
- **Spring Boot**
- **Spring Security**
- **Maven**
- **PostgreSQL**
- **Testcontainers**
- **Docker**
- **Swagger / OpenAPI**
- **Neon** — PostgreSQL database hosting
- **Render** — Application deployment

## API Endpoints

| Method | Endpoint              | Description         | Access        |
| ------ | --------------------- | ------------------- | ------------- |
| POST   | `/api/auth/register`  | Register a new user | Public        |
| POST   | `/api/auth/login`     | Authenticate a user | Public        |
| GET    | `/api/employees`      | Get all employees   | Authenticated |
| GET    | `/api/employees/{id}` | Get an employee     | Authenticated |
| POST   | `/api/employees`      | Create an employee  | Admin         |
| PUT    | `/api/employees/{id}` | Update an employee  | Admin         |
| DELETE | `/api/employees/{id}` | Delete an employee  | Admin         |

## API Documentation

Interactive API documentation is available through Swagger UI

## Testing

The project includes integration tests covering API endpoints, validation, authentication, authorization, and exception handling.

Tests use **Testcontainers** to run against a PostgreSQL database in a containerized environment.

## Deployment

The application is containerized with Docker and deployed using Render, with PostgreSQL hosted through Neon.
