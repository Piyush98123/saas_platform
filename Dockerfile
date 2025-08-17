FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy Gradle files
COPY build.gradle settings.gradle ./
COPY gradle/ gradle/
COPY gradlew ./

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src/ src/

# Build the application
RUN ./gradlew build -x test --no-daemon

# Create runtime image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR
COPY --from=0 /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8090

# Health check (fixed port to match EXPOSE)
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8090/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]







