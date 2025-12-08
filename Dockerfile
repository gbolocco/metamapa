# Imagen base con Maven + JDK 17 (trae todo lo necesario)
FROM maven:3.9.6-eclipse-temurin-17

# Directorio de trabajo dentro del contenedor
WORKDIR /workspace

COPY . .

# Dejar la terminal abierta por defecto
CMD [ "bash" ]

