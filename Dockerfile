FROM openjdk:8-jdk-alpine
ARG version
ADD target/msj-user-${version}.jar app.jar
ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar --spring.profiles.active=prod