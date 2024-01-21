# Use a Gradle image with the JDK for building the project
FROM gradle:8.1.1-jdk17 AS build

# Install protobuf compiler
USER root
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    protobuf-compiler
USER gradle

# Copy project files to the container
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# Build the project, skipping tests
RUN ./gradlew build -x test --no-daemon
# Use an OpenJDK image for running the application
FROM openjdk:17-jdk

# Expose the port the application runs on
EXPOSE 8002

# Create a directory to hold the application
RUN mkdir /app

# Copy the built JAR file into the image
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar

# Specify the command to run the application
CMD ["java", "-jar", "/app/app.jar"]
