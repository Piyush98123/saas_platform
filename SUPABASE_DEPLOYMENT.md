# 🚀 Supabase Deployment Guide

## 📋 **Step 1: Set Up Supabase Project**

### 1.1 Create Supabase Account
- Go to [supabase.com](https://supabase.com)
- Sign up/Login with GitHub
- Click "New Project"

### 1.2 Configure Project
- **Name:** `saas-platform`
- **Database Password:** Create a strong password (save this!)
- **Region:** Choose closest to your users
- **Pricing Plan:** Start with Free tier

### 1.3 Get Connection Details
After project creation, go to **Settings → Database**:
- **Host:** `db.xxxxxxxxxxxxx.supabase.co`
- **Database:** `postgres`
- **Port:** `5432`
- **User:** `postgres`
- **Password:** (the one you created)

## 🔧 **Step 2: Configure Environment Variables**

### 2.1 Create Environment File
Create `backend/.env` file:

```bash
# Supabase Database Configuration
DB_URL=jdbc:postgresql://db.xxxxxxxxxxxxx.supabase.co:5432/postgres
DB_USER=postgres
DB_PASSWORD=your_supabase_password
DB_DRIVER_CLASS_NAME=org.postgresql.Driver

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key-make-it-long-and-secure
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# Server Configuration
SERVER_PORT=8090

# Email Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:19006
```

### 2.2 Update application.yml
The main `application.yml` now supports both H2 and PostgreSQL:

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:h2:mem:testdb}
    username: ${DB_USER:sa}
    password: ${DB_PASSWORD:}
    driver-class-name: ${DB_DRIVER_CLASS_NAME:org.h2.Driver}
```

## 🗄️ **Step 3: Database Migration**

### 3.1 Enable Flyway
Update `application.yml` when using Supabase:

```yaml
spring:
  flyway:
    enabled: true
    url: ${DB_URL}
    user: ${DB_USER}
    password: ${DB_PASSWORD}
    baseline-on-migrate: true
```

### 3.2 Run Migrations
```bash
cd backend
./gradlew flywayMigrate
```

## 🚀 **Step 4: Deploy Backend**

### 4.1 Local Testing with Supabase
```bash
# Set environment variables
$env:DB_URL="jdbc:postgresql://db.xxxxxxxxxxxxx.supabase.co:5432/postgres"
$env:DB_USER="postgres"
$env:DB_PASSWORD="your_supabase_password"
$env:DB_DRIVER_CLASS_NAME="org.postgresql.Driver"

# Run backend
./gradlew bootRun
```

### 4.2 Production Deployment
Use the `application-prod.yml` profile:

```bash
# Set active profile
$env:SPRING_PROFILES_ACTIVE="prod"

# Run with production config
./gradlew bootRun
```

## 🔒 **Step 5: Security Configuration**

### 5.1 Supabase Row Level Security (RLS)
Enable RLS in Supabase SQL Editor:

```sql
-- Enable RLS on all tables
ALTER TABLE users ENABLE ROW LEVEL SECURITY;
ALTER TABLE companies ENABLE ROW LEVEL SECURITY;
ALTER TABLE leads ENABLE ROW LEVEL SECURITY;
ALTER TABLE quotes ENABLE ROW LEVEL SECURITY;
ALTER TABLE bookings ENABLE ROW LEVEL SECURITY;
ALTER TABLE system_settings ENABLE ROW LEVEL SECURITY;

-- Create policies for tenant isolation
CREATE POLICY "Users can only access their own tenant data" ON users
    FOR ALL USING (tenant_id = current_setting('app.tenant_id')::text);

CREATE POLICY "Companies can only access their own data" ON companies
    FOR ALL USING (tenant_id = current_setting('app.tenant_id')::text);

-- Add more policies as needed
```

### 5.2 Connection Pooling
Add to `application.yml`:

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
```

## 📊 **Step 6: Monitoring & Health Checks**

### 6.1 Database Health Check
```bash
# Test connection
curl http://localhost:8090/api/actuator/health
```

### 6.2 Supabase Dashboard
- Monitor database performance
- Check connection pool usage
- View query performance

## 🚨 **Step 7: Troubleshooting**

### 7.1 Common Issues

**Connection Refused:**
- Check if Supabase project is active
- Verify connection string format
- Ensure IP is not blocked

**Authentication Failed:**
- Verify username/password
- Check if database exists
- Ensure user has proper permissions

**SSL Issues:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://db.xxxxxxxxxxxxx.supabase.co:5432/postgres?sslmode=require
```

### 7.2 Performance Optimization
```yaml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
        batch_versioned_data: true
```

## 🔄 **Step 8: Switch Between Environments**

### 8.1 Development (H2)
```bash
# No environment variables needed
./gradlew bootRun
```

### 8.2 Production (Supabase)
```bash
# Set Supabase environment variables
$env:SPRING_PROFILES_ACTIVE="prod"
./gradlew bootRun
```

## 📝 **Step 9: Environment Variables Summary**

| Variable | Description | Example |
|----------|-------------|---------|
| `DB_URL` | Supabase connection string | `jdbc:postgresql://db.xxx.supabase.co:5432/postgres` |
| `DB_USER` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `your_password` |
| `DB_DRIVER_CLASS_NAME` | JDBC driver | `org.postgresql.Driver` |
| `SPRING_PROFILES_ACTIVE` | Active profile | `prod` |

## ✅ **Step 10: Verification**

1. **Database Connection:** Backend starts without errors
2. **Tables Created:** Check Supabase dashboard for tables
3. **API Endpoints:** Test `/api/health` endpoint
4. **Data Persistence:** Create/read data through your API

## 🎯 **Next Steps**

- [ ] Set up Supabase project
- [ ] Configure environment variables
- [ ] Test database connection
- [ ] Run database migrations
- [ ] Deploy backend with Supabase
- [ ] Configure RLS policies
- [ ] Set up monitoring

Your backend is now ready to use Supabase PostgreSQL! 🚀
