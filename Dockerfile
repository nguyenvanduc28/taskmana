# Use an official Maven image with OpenJDK 17 to build the application
FROM maven:3.8.7-openjdk-18-slim AS build
WORKDIR /app

# Copy the pom.xml and the source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use an official OpenJDK image to run the application
FROM openjdk:18-ea-10-jdk-slim
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/taskbe-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]