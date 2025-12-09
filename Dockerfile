# --- STAGE 1: BUILD ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline # Descargar dependencias primero

COPY src /app/src
RUN mvn clean package -DskipTests # Compilar y generar el JAR

# --- STAGE 2: RUNTIME ---
# Usar una imagen más ligera y soportada para la ejecución
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiar el JAR generado de la etapa 'build'
# NOTA: Asegúrate de que el nombre coincida con tu artifactId-version en pom.xml
COPY --from=build /app/target/ejercicio-1.0-SNAPSHOT.jar /app/app.jar 
COPY datos /app/datos 

# Exponer el puerto de Javalin (8080)
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]
