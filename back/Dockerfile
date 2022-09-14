
FROM gradle:jdk18 as builder

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src


RUN gradle clean build -x test

FROM openjdk:10-jre-slim as finalApp

ENTRYPOINT ["sudo", "mkdir", "/app/"]

WORKDIR app


COPY --from=builder /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar"]