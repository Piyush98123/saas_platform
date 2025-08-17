# SaaS Platform Backend

Spring Boot backend application providing a robust, multi-tenant API for service business management.

## 🚀 Features

- **Multi-Tenancy**: Row-level isolation with tenant resolution via subdomain/header
- **JWT Authentication**: Secure token-based authentication with refresh tokens
- **Role-Based Access Control**: SUPER_ADMIN, COMPANY_ADMIN, STAFF, CUSTOMER roles
- **Workflow Engine**: Camunda BPM integration for customizable business processes
- **Scheduling**: Quartz integration for automated tasks and reminders
- **API Documentation**: OpenAPI/Swagger integration
- **Database Migrations**: Flyway for schema versioning

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.2.0
- **Java Version**: 17
- **Security**: Spring Security with JWT
- **Database**: PostgreSQL with JPA/Hibernate
- **Migrations**: Flyway
- **Workflow**: Camunda BPM
- **Scheduling**: Quartz
- **Documentation**: OpenAPI/Swagger
- **Build Tool**: Gradle

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/saasplatform/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── entity/          # JPA entities
│   │   ├── repository/      # Data repositories
│   │   ├── service/         # Business logic services
│   │   ├── dto/            # Data transfer objects
│   │   ├── security/       # Security configuration
│   │   ├── workflow/       # Workflow engine integration
│   │   ├── notification/   # Notification services
│   │   └── integration/    # Third-party integrations
│   └── resources/
│       ├── application.yml  # Application configuration
│       ├── openapi.yaml    # OpenAPI specification
│       └── db/migration/   # Flyway migrations
└── test/                   # Test classes
```

## 🚀 Quick Start

### Prerequisites

- Java 17+
- PostgreSQL 13+
- Gradle 7.6+

### Environment Setup

1. **Create environment variables:**
   ```bash
   export DB_URL=jdbc:postgresql://localhost:5432/saas_platform
   export DB_USER=postgres
   export DB_PASSWORD=postgres
   export JWT_SECRET=your-secret-key-here-make-it-long-and-secure
   export JWT_EXPIRATION=86400000
   export JWT_REFRESH_EXPIRATION=604800000
   ```

2. **Create PostgreSQL database:**
   ```sql
   CREATE DATABASE saas_platform;
   ```

### Running the Application

1. **Build the project:**
   ```bash
   ./gradlew build
   ```

2. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

3. **Access the application:**
   - API Base: http://localhost:8080/api
   - Swagger UI: http://localhost:8080/swagger-ui
   - Health Check: http://localhost:8080/actuator/health
   - OpenAPI Spec: http://localhost:8080/v3/api-docs

## 🔐 Authentication

### JWT Configuration

- **Access Token**: 24 hours (configurable)
- **Refresh Token**: 7 days (configurable)
- **Algorithm**: HS512

### Demo Users

The application comes with pre-seeded demo data:

- **Demo Company** (tenant: `demo`)
  - Admin: `admin@demoservice.com` / `password123`
  - Staff: `staff@demoservice.com` / `password123`

- **Platform Admin** (tenant: `platform`)
  - Super Admin: `superadmin@saasplatform.com` / `password123`

## 📱 Multi-Tenancy

### Tenant Resolution

1. **Subdomain**: `{tenant}.app.com`
2. **Header**: `X-Tenant-ID: {tenant}`
3. **Database**: Row-level isolation via `tenant_id` column

### Supported Tenants

- `demo` - Demo service company
- `platform` - Platform administration

## 🗄️ Database

### Schema

The database schema includes:

- **Companies**: Tenant information and settings
- **Users**: User accounts with role assignments
- **Roles**: Role definitions and permissions
- **Customers**: Customer information and relationships
- **Leads**: Sales leads and prospects
- **Quotes**: Customer quotes and proposals
- **Bookings**: Scheduled appointments and services

### Migrations

Database migrations are managed by Flyway:

- `V1__Initial_Schema.sql` - Core schema creation
- `V2__Seed_Data.sql` - Demo data seeding

## 🔒 Security

### Role-Based Access Control

- **SUPER_ADMIN**: Platform-wide access, company management
- **COMPANY_ADMIN**: Company-wide access, user management
- **STAFF**: Limited access, job management
- **CUSTOMER**: Customer-specific data access

### Security Features

- JWT token validation
- Role-based method security
- Tenant isolation
- CORS configuration
- Input validation

## 📊 API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/refresh` - Token refresh

### Companies
- `GET /api/companies` - List companies (Super Admin)
- `POST /api/companies` - Create company (Super Admin)

### Users
- `GET /api/users` - List users (Company Admin)

### Customers
- `GET /api/customers` - List customers
- `POST /api/customers` - Create customer

### Leads
- `GET /api/leads` - List leads

### Quotes
- `GET /api/quotes` - List quotes

### Bookings
- `GET /api/bookings` - List bookings

### Public Endpoints
- `GET /api/public/quote/{token}` - View quote by approval token
- `POST /api/public/quote/{token}` - Approve/reject quote

## 🧪 Testing

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests *UserServiceTest

# Run with coverage
./gradlew test jacocoTestReport
```

### Test Configuration

- Uses H2 in-memory database for tests
- MockMVC for controller testing
- JUnit 5 for unit tests

## 🚀 Deployment

### Production Build

```bash
./gradlew build -x test
```

### Docker

```bash
# Build Docker image
docker build -t saas-platform-backend .

# Run container
docker run -p 8080:8080 saas-platform-backend
```

### Environment Variables

Required environment variables for production:

```bash
DB_URL=jdbc:postgresql://your-db-host:5432/saas_platform
DB_USER=your_db_user
DB_PASSWORD=your_db_password
JWT_SECRET=your-very-long-and-secure-jwt-secret
SERVER_PORT=8080
```

## 🔧 Configuration

### Application Properties

Key configuration options in `application.yml`:

- Database connection settings
- JWT configuration
- CORS settings
- Tenant resolution
- Integration API keys
- Logging levels

### Customization

- Modify `application.yml` for environment-specific settings
- Override with environment variables
- Use Spring profiles for different environments

## 📚 API Documentation

### Swagger UI

Access the interactive API documentation at `/swagger-ui` when running.

### OpenAPI Specification

The API specification is defined in `src/main/resources/openapi.yaml` and can be used to:

- Generate client libraries
- Import into API testing tools
- Generate documentation

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection**: Ensure PostgreSQL is running and accessible
2. **JWT Secret**: Use a long, secure secret key
3. **Port Conflicts**: Check if port 8080 is available
4. **Tenant Resolution**: Verify tenant ID in headers or subdomain

### Logs

Enable debug logging by setting:

```yaml
logging:
  level:
    com.saasplatform: DEBUG
```

## 🤝 Contributing

1. Follow the existing code style
2. Add tests for new functionality
3. Update documentation as needed
4. Ensure all tests pass before submitting

## 📄 License

This project is licensed under the MIT License.







