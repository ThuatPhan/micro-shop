# -------- Phase 1: Build --------
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# -------- Phase 2: Runtime --------
FROM openjdk:17-jdk-slim
WORKDIR /app
RUN apt-get update && apt-get install -y curl
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
