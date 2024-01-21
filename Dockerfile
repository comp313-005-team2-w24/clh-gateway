# Do not use it! Please do ./gradlew jib
FROM gradle:8.1.1-jdk17 AS build

USER root
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    protobuf-compiler
USER gradle

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN ./gradlew build -x test --no-daemon
# Use an OpenJDK image for running the application
FROM openjdk:17-jdk

EXPOSE 8002

RUN mkdir /app
# Copy the built JAR file into the image
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]
