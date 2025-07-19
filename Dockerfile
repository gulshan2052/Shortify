# Use official OpenJDK 21 image
FROM eclipse-temurin:21-jre

# Set working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/Shortify.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=docker

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]