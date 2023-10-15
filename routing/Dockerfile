FROM sbtscala/scala-sbt:eclipse-temurin-focal-17.0.8.1_1_1.9.6_2.13.12 AS build

COPY . /root
WORKDIR /root

RUN sbt routing/assembly


FROM eclipse-temurin:17-jre-focal

RUN mkdir /opt/routing-service
COPY --from=build /root/routing/target/scala-2.13/routing.jar /opt/routing-service/routing.jar
ENTRYPOINT ["java", "-jar", "/opt/routing-service/routing.jar"]
