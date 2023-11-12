FROM sbtscala/scala-sbt:eclipse-temurin-focal-17.0.8.1_1_1.9.6_2.13.12 AS build

COPY . /root
WORKDIR /root

RUN sbt image/assembly

FROM eclipse-temurin:17-jre-focal

RUN mkdir /opt/image-service
COPY --from=build /root/image/target/scala-2.13/image.jar /opt/image-service/image.jar
ENTRYPOINT ["java", "-jar", "/opt/image-service/image.jar"]
