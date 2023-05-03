FROM sbtscala/scala-sbt:eclipse-temurin-jammy-19.0.1_10_1.8.2_3.2.2 AS build
WORKDIR /root
RUN sbt assembly

FROM eclipse-temurin:19-jre-focal

RUN mkdir -p /build/routing

COPY --from=build /root/auth/target/*.jar /build/routing.jar
ENTRYPOINT ["java", "-jar", "/build/routing/routing.jar"]
