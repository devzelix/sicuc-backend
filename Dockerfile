# -----------------------------------------------------------
# STAGE 1: COMPILACIÓN Y DESARROLLO (BUILDER/DEV)
# Esta etapa sirve como el entorno de trabajo interactivo para VS Code.
# -----------------------------------------------------------
FROM eclipse-temurin:21-jdk-alpine AS builder

# Instalar herramientas de desarrollo
RUN apk add --no-cache maven git

# Directorio de trabajo
WORKDIR /app

# 1. Copiar el POM y descargar dependencias (para optimizar el caché)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copiar el código fuente restante
COPY . .

# Comando de compilación (Solo se ejecuta durante el Build para generar el JAR)
# Se deja aquí para que Docker pueda cachear el JAR si es necesario.
RUN mvn clean package -DskipTests

# 🔑 CLAVE PARA DEV: El comando final (CMD) para esta etapa es 'sh', 
# permitiendo a VS Code inyectar su propia conexión y comandos.
# Esto asegura que el contenedor Dev Container se inicie en un shell interactivo.
CMD ["sh"]

# -----------------------------------------------------------
# STAGE 2: EJECUCIÓN EN PRODUCCIÓN (RUNTIME)
# Esta etapa solo se usa cuando se construye la imagen final para PROD.
# -----------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# Copiar el JAR compilado desde la etapa 'builder'
COPY --from=builder /app/target/*.jar app.jar

# Exponer el puerto de Spring Boot
EXPOSE 8080

# Comando de ejecución de la aplicación final (Para PROD)
ENTRYPOINT ["java", "-jar", "app.jar"]