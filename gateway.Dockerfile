FROM maven:3.6.0-jdk-11-slim AS build
COPY /gateway-service/src /home/gateway/my/src
COPY /gateway-service/pom.xml /home/gateway/my
COPY ./pom.xml /home/gateway
RUN mvn -f /home/gateway/my/pom.xml clean package

FROM openjdk:11
COPY --from=build /home/gateway/my/target/gateway-service-1.0-SNAPSHOT.jar gateway-service-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","gateway-service-1.0-SNAPSHOT.jar"]