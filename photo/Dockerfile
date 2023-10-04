FROM sbtscala/scala-sbt:eclipse-temurin-17.0.5_8_1.8.2_2.13.10 AS build

COPY . /root
WORKDIR /root

RUN sbt photo/assembly


FROM eclipse-temurin:17-jre-focal

RUN mkdir /opt/photo-service
COPY --from=build /root/photo/target/scala-2.13/project-photo-assembly-0.1.0-SNAPSHOT.jar /opt/photo-service/photo.jar
ENTRYPOINT ["java", "-jar", "/opt/photo-service/photo.jar"]