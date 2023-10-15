FROM sbtscala/scala-sbt:eclipse-temurin-focal-17.0.8.1_1_1.9.6_2.13.12 AS build

COPY . /root
WORKDIR /root

RUN sbt auth/assembly


FROM eclipse-temurin:17-jre-focal

RUN mkdir /opt/auth-service
COPY --from=build /root/auth/target/scala-2.13/auth.jar /opt/auth-service/auth.jar
ENTRYPOINT ["java", "-jar", "/opt/auth-service/auth.jar"]
