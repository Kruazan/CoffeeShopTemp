# Этап сборки
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app

# Копируем только файлы, необходимые для сборки
COPY build.gradle.kts ./
COPY settings.gradle.kts ./
COPY gradle ./gradle
COPY gradlew ./
COPY src ./src

# Сборка jar-файла без тестов
RUN ./gradlew clean build -x test

# Финальный образ
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Копируем jar-файл из билдера
COPY --from=builder /app/build/libs/*.jar app.jar

# Открываем порт 8080
EXPOSE 8080

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
