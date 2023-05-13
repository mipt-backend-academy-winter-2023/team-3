FROM sbtscala/scala-sbt:eclipse-temurin-17.0.5_8_1.8.2_2.13.10 AS build

COPY . /root
WORKDIR /root

RUN sbt assembly


FROM eclipse-temurin:17-jre-focal

RUN mkdir /opt/routing-service
COPY --from=build /root/routing/target/scala-2.13/routing.jar /opt/routing-service/routing.jar
ENTRYPOINT ["java", "-jar", "/opt/routing-service/routing.jar"]
