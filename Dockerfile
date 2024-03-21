# Use JDK 17 slim image for the build stage
 FROM openjdk:17-jdk-slim AS build

# Copy the project files to the container
COPY . /app

# Set the working directory
WORKDIR /app

# Make the gradlew script executable
RUN chmod +x gradlew

# Run the Gradle build
RUN ./gradlew build

# Use JDK 17 slim image for the runtime environment
FROM openjdk:17.0.1-jdk-slim

# Expose port 8080
EXPOSE 8082

# # Create a directory for the application
# RUN mkdir /app

# # Copy the built JAR from the build stage to the runtime stage
# COPY --from=build /Vendor.0.0.1-SNAPSHOT.jar /spring-boot-application.jar

# # Set the entry point to run the Spring Boot application
# ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar"]

# Create a directory for the application
RUN mkdir /app

# Copy the JAR file from the root directory of your repository to the /app directory in the container
COPY Vendor-0.0.1-SNAPSHOT.jar /app/spring-boot-application.jar

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spring-boot-application.jar"]


