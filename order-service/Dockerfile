# Use the official Spring Boot image as a base image
FROM openjdk:17-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY build/libs/order-service-0.0.1-SNAPSHOT.jar app.jar

ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar ./

ENV JAVA_TOOL_OPTIONS "-javaagent:./opentelemetry-javaagent.jar"

# Expose the port that the application will run on
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "app.jar"]
