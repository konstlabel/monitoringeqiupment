FROM eclipse-temurin:24-jdk-alpine AS builder
WORKDIR /build

# 1. Копируем только mvnw сначала
COPY mvnw ./

# 2. Создаем структуру каталогов и копируем wrapper файлы
COPY .mvn/wrapper/maven-wrapper.jar .mvn/wrapper/maven-wrapper.properties .mvn/wrapper/

# 3. Устанавливаем права
RUN chmod +x mvnw

# 4. Копируем остальные файлы
COPY pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw clean package -DskipTests -e

FROM eclipse-temurin:24-jdk-alpine
WORKDIR /app
COPY --from=builder /build/target/*.jar ./app.jar
EXPOSE 53828
ENTRYPOINT ["java","-jar","app.jar"]