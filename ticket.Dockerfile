FROM maven:3.6.0-jdk-11-slim AS build
COPY /ticket-service/src /home/ticket/my/src
COPY /ticket-service/pom.xml /home/ticket/my
COPY ./pom.xml /home/ticket
RUN mvn -f /home/ticket/my/pom.xml clean package

FROM openjdk:11
COPY --from=build /home/ticket/my/target/ticket-service-1.0-SNAPSHOT.jar ticket-service-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","ticket-service-1.0-SNAPSHOT.jar"]