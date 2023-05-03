FROM sbtscala/scala-sbt:eclipse-temurin-jammy-19.0.1_10_1.8.2_3.2.2 AS build
COPY . /service
WORKDIR /service
RUN sbt assembly

FROM eclipse-temurin:19-jre-focal

RUN mkdir /build

COPY --from=build /service/target/*.jar /build/service.jar
ENTRYPOINT ["java", "-jar", "/build/service.jar"]
