# ===== BUILD =====
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Cacheo de dependencias
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Compila
COPY src ./src
RUN mvn -q -DskipTests package

# ===== RUNTIME =====
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia el JAR final (ajusta el patrón si tu artefacto tiene otro nombre)
COPY --from=build /workspace/target/*.jar /app/app.jar

# Usuario no root
RUN useradd -r -u 1001 spring && chown -R spring:spring /app
USER spring

# Flags JVM amigables con 512MB RAM del tier free
ENV JAVA_OPTS="-Xms64m -Xmx256m -XX:MaxRAMPercentage=60 -XX:+UseSerialGC -Dfile.encoding=UTF-8"

# Koyeb te da PORT; lo mapeamos a Spring (SERVER_PORT)
ENTRYPOINT ["sh","-c","exec env SERVER_PORT=${PORT:-8080} java $JAVA_OPTS -jar /app/app.jar"]

