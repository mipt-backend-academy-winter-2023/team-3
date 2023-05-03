FROM sbtscala/scala-sbt:eclipse-temurin-jammy-19.0.1_10_1.8.2_3.2.2 AS build
WORKDIR /root
RUN sbt assembly

FROM eclipse-temurin:19-jre-focal

RUN mkdir -p /build/helper

COPY --from=build /root/auth/target/*.jar /build/helper.jar
ENTRYPOINT ["java", "-jar", "/build/helper/helper.jar"]
