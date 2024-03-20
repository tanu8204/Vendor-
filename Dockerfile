# Use JDK 17 instead of JDK 8
FROM openjdk:17-jdk-slim AS build


# Copy the project files to the container
COPY . .

# Set the working directory
# WORKDIR  /home/gradle/src/

# Run the Gradle build
RUN gradle build

# Use JDK 17 slim image for the runtime environment
FROM openjdk:17.0.1-jdk-slim

# Expose port 8080
EXPOSE 8082

# Create a directory for the application
RUN mkdir /app

# Copy the built JAR from the build stage to the runtime stage
COPY --from=build /gradle/src/build/libs/Vendor.0.0.1-SNAPSHOT.jar /app/spring-boot-application.jar

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java","-jar", "/app/spring-boot-application.jar"]

CMD ["/bin/bash"]
