FROM gcr.io/distroless/java21-debian12
COPY target/Shortify.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
