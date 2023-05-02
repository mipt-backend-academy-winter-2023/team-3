FROM sbtscala/scala-sbt:eclipse-temurin-17.0.4_1.7.1_3.2.0

COPY . /root
WORKDIR /root

RUN sbt assembly

RUN mkdir /opt/routing-service
RUN cp /root/auth/target/scala-2.13/auth.jar /opt/routing-service/auth.jar
RUN cp /root/routing/target/scala-2.13/routing.jar /opt/routing-service/routing.jar
