# Этап сборки
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
COPY build.gradle settings.gradle.kts gradle.properties ./
COPY gradle ./gradle
COPY src ./src
RUN gradle clean build -x test --no-daemon

# Финальный образ
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]