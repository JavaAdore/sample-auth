#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/sample-auth-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]

# docker build . -t meltaieb/sample-auth:01
# docker run -p 8080:9090 meltaieb/sample-auth:01

# docker ps -al
# docker commit ########## meltaieb/sample-auth:01

# docker push meltaieb/sample-auth:01

# docker login -u meltaieb
# password is access token

# => kuernates local -- install kubernates (minikube, kubectl )
# => managed (cloud) --> managed by provider (aws , google, digital ocean)

# components ( deployment, service , pod, secret, .... )  Ingres component installtion on kubernates cluster