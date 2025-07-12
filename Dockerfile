# === этап сборки ===
FROM eclipse-temurin:24-jdk-alpine AS builder
WORKDIR /build

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw clean package -DskipTests

# === финальный образ ===
FROM eclipse-temurin:24-jdk-alpine
WORKDIR /app
COPY --from=builder /build/target/monitoringeqiupment-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
