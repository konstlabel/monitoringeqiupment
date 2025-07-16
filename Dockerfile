# ==============================================================================
# Stage 1: Build the Java application using Maven (Multi-stage build)
# ==============================================================================

# Use a lightweight OpenJDK 24 base image with Alpine Linux
FROM eclipse-temurin:24-jdk-alpine AS builder

# Set the working directory inside the container for build operations
WORKDIR /build

# 1. Copy only the Maven wrapper script first (for faster caching)
COPY mvnw ./

# 2. Copy Maven wrapper JAR and configuration files
COPY .mvn/wrapper/maven-wrapper.jar .mvn/wrapper/maven-wrapper.properties .mvn/wrapper/

# 3. Make the Maven wrapper executable
RUN chmod +x mvnw

# 4. Copy the project descriptor (pom.xml)
COPY pom.xml ./

# 5. Download all project dependencies (offline mode) to speed up future builds
RUN ./mvnw dependency:go-offline

# 6. Copy the application source code
COPY src src

# 7. Build the application without running tests
RUN ./mvnw clean package -DskipTests -e


# ==============================================================================
# Stage 2: Run the packaged application
# ==============================================================================

# Use a clean base image to run the app (no build tools included)
FROM eclipse-temurin:24-jdk-alpine

# Set the working directory for the running container
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /build/target/*.jar ./app.jar

# Expose the port the application will listen on (as defined in application.properties)
EXPOSE 53828

# Define the entrypoint command to run the application
ENTRYPOINT ["java","-jar","app.jar"]
