FROM maven:3.6.0-jdk-11-slim AS build
COPY /auth-service/src /home/auth/my/src
COPY /auth-service/pom.xml /home/auth/my
COPY ./pom.xml /home/auth
RUN mvn -f /home/auth/my/pom.xml clean package

FROM openjdk:11
COPY --from=build /home/auth/my/target/auth-service-1.0-SNAPSHOT.jar auth-service-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","auth-service-1.0-SNAPSHOT.jar"]