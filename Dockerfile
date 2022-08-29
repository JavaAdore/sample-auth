
#
# Package stage
#
FROM openjdk:11-jre-slim
COPY ./target/sample-auth-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]