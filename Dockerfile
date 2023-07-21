FROM openjdk:17-jdk-alpine
EXPOSE 8050
ARG JAR_FILE=target/revurestapi-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

