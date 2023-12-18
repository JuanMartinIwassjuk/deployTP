# syntax = docker/dockerfile:1.2
#
# Build stage
#
FROM maven:3.8.6-openjdk-18 AS build
COPY . .
RUN mvn clean package assembly:single -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /target/ejercicio-1.0-SNAPSHOT-jar-with-dependencies.jar ejercicio-1.0-SNAPSHOT.jar
# ENV PORT=8090
EXPOSE 8080
CMD ["java","-classpath","ejercicio-1.0-SNAPSHOT.jar","ar/edu/utn/frba/dds/server/App"]