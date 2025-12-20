# Multi-stage build para optimizar tamaño de imagen

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiar archivos de configuración Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Descargar dependencias (se cachea esta capa)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Construir el JAR (sin tests para más rapidez)
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar el JAR del stage anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando de inicio con opciones de JVM
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
