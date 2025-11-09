# ===================================================================
# == MULTI-STAGE BUILD CONFIGURATION
# ===================================================================

# This pattern ensures the final production image (runtime) is minimal 
# and secure, as it only contains the JRE and the final compiled JAR.

# -----------------------------------------------------------
# STAGE 1: COMPILATION AND DEVELOPMENT (BUILDER/DEV)
# Base: Uses the full Java Development Kit (JDK) on a minimal Alpine image.
# This stage serves as the interactive development and compilation environment.
# -----------------------------------------------------------
FROM eclipse-temurin:21-jdk-alpine AS builder

# Install necessary development tools (Maven, Git)
RUN apk add --no-cache maven git

# Set the working directory
WORKDIR /app

# 1. Copy POM and download dependencies for efficient caching.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copy the source code
COPY . .

# Final compilation command (only runs during the build process)
# DskipTests is used because tests are run separately by the CI pipeline.
RUN mvn clean package -DskipTests

# KEY FOR DEV: CMD is 'sh', allowing VS Code/Dev Containers 
# to inject their own connection and commands for interactive work.
CMD ["sh"]

# -----------------------------------------------------------
# STAGE 2: PRODUCTION EXECUTION (RUNTIME)
# Base: Uses the minimal Java Runtime Environment (JRE) on Alpine.
# This stage builds the final, secure, and compact production image.
# -----------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# CRITICAL: Copy only the compiled application JAR from the 'builder' stage.
# This discards all development tools (JDK, Maven, Git), resulting in a small image.
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Command to execute the final application (For PROD)
# ENTRYPOINT ensures the application starts immediately when the container launches.
ENTRYPOINT ["java", "-jar", "app.jar"]