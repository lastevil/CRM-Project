FROM maven:3.6.0-jdk-11-slim AS build
COPY /discovery-service/src /home/discovery/my/src
COPY /discovery-service/pom.xml /home/discovery/my
COPY ./pom.xml /home/discovery
RUN mvn -f /home/discovery/my/pom.xml clean package

FROM openjdk:11
COPY --from=build /home/discovery/my/target/discovery-service-1.0-SNAPSHOT.jar discovery-service-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","discovery-service-1.0-SNAPSHOT.jar"]