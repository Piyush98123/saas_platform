# 🚀 Supabase Setup Script for Windows PowerShell
# Run this script to configure your environment for Supabase

Write-Host "🚀 Setting up Supabase environment variables..." -ForegroundColor Green

# Get Supabase connection details from user
Write-Host "`n📋 Please enter your Supabase connection details:" -ForegroundColor Yellow

$supabaseHost = Read-Host "Enter your Supabase host (e.g., db.xxxxxxxxxxxxx.supabase.co)"
$supabasePassword = Read-Host "Enter your Supabase database password" -AsSecureString
$supabasePassword = [Runtime.InteropServices.Marshal]::PtrToStringAuto([Runtime.InteropServices.Marshal]::SecureStringToBSTR($supabasePassword))

# Construct the database URL
$dbUrl = "jdbc:postgresql://$supabaseHost:5432/postgres"

Write-Host "`n🔧 Setting environment variables..." -ForegroundColor Blue

# Set environment variables
$env:DB_URL = $dbUrl
$env:DB_USER = "postgres"
$env:DB_PASSWORD = $supabasePassword
$env:DB_DRIVER_CLASS_NAME = "org.postgresql.Driver"
$env:SPRING_PROFILES_ACTIVE = "prod"

# Set other common environment variables
$env:JWT_SECRET = "your-super-secret-jwt-key-make-it-long-and-secure-in-production"
$env:JWT_EXPIRATION = "86400000"
$env:JWT_REFRESH_EXPIRATION = "604800000"
$env:SERVER_PORT = "8090"

Write-Host "`n✅ Environment variables set successfully!" -ForegroundColor Green
Write-Host "`n📊 Current configuration:" -ForegroundColor Cyan
Write-Host "DB_URL: $env:DB_URL" -ForegroundColor White
Write-Host "DB_USER: $env:DB_USER" -ForegroundColor White
Write-Host "DB_DRIVER_CLASS_NAME: $env:DB_DRIVER_CLASS_NAME" -ForegroundColor White
Write-Host "SPRING_PROFILES_ACTIVE: $env:SPRING_PROFILES_ACTIVE" -ForegroundColor White

Write-Host "`n🚀 You can now run your backend with:" -ForegroundColor Yellow
Write-Host "./gradlew bootRun" -ForegroundColor White

Write-Host "`n⚠️  Note: These environment variables are only set for the current PowerShell session." -ForegroundColor Red
Write-Host "For permanent setup, add them to your system environment variables or use a .env file." -ForegroundColor Red

Write-Host "`n🎯 Next steps:" -ForegroundColor Green
Write-Host "1. Test the connection: ./gradlew bootRun" -ForegroundColor White
Write-Host "2. Run migrations: ./gradlew flywayMigrate" -ForegroundColor White
Write-Host "3. Check health endpoint: http://localhost:8090/api/actuator/health" -ForegroundColor White

Write-Host "`n✨ Setup complete! Happy coding!" -ForegroundColor Green


