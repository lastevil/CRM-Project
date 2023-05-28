FROM maven:3.6.0-jdk-11-slim AS build
COPY /analytic-service/src /home/analytic/my/src
COPY /analytic-service/pom.xml /home/analytic/my
COPY ./pom.xml /home/analytic
RUN mvn -f /home/analytic/my/pom.xml clean package

FROM openjdk:11
COPY --from=build /home/analytic/my/target/analytic-service-1.0-SNAPSHOT.jar analytic-service-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","analytic-service-1.0-SNAPSHOT.jar"]