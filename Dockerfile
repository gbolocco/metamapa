# --- STAGE 1: BUILD ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline # Descargar dependencias primero

COPY src /app/src
RUN mvn clean package -DskipTests # Compilar y generar el JAR

# --- STAGE 2: RUNTIME ---
# Usar una imagen más ligera para la ejecución
FROM openjdk:17-jre-slim
WORKDIR /app

# Copiar el JAR generado de la etapa 'build'
COPY --from=build /app/target/tp-dds-1.0-SNAPSHOT.jar /app/app.jar 

# Exponer el puerto de Javalin (8080)
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]
