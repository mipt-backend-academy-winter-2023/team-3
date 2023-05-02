FROM sbtscala/scala-sbt:eclipse-temurin-jammy-19.0.1_10_1.8.2_3.2.2 AS build
COPY . /root/
WORKDIR /root
ENV SBT_OPTS="-Xms4G -Xmx4G -Xss8M"
RUN sbt assembly

FROM eclipse-temurin:19-jre-focal
RUN mkdir /build
COPY --from=build /root/target/*.jar /build/hw.jar
ENTRYPOINT ["java", "-jar", "/build/hw.jar"]
