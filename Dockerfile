# ===== BUILD STAGE =====
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /workspace

# Copiar solo archivos de dependencias primero (mejor cache)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Descargar dependencias (esta capa se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar aplicación (skip tests para build más rápido)
RUN mvn clean package -DskipTests -B

# ===== RUNTIME STAGE =====
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Instalar dumb-init para mejor manejo de señales
RUN apk add --no-cache dumb-init

# Copiar JAR desde build stage
COPY --from=build /workspace/target/*.jar app.jar

# Crear usuario no privilegiado
RUN addgroup -S spring && adduser -S spring -G spring && \
    chown -R spring:spring /app

# Cambiar a usuario no root
USER spring:spring

# Variables de entorno optimizadas para contenedor
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+UseG1GC \
    -XX:+OptimizeStringConcat \
    -Djava.security.egd=file:/dev/./urandom \
    -Dfile.encoding=UTF-8"

# Puerto por defecto
EXPOSE 8080

# Usar dumb-init como PID 1
ENTRYPOINT ["dumb-init", "--"]

# Comando de inicio
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]