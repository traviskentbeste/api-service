# Pre-release / non-production builds of OpenJDK
FROM openjdk:19-jdk-alpine

# our target/jar file
ARG JAR_FILE=target/*.jar

# copy over to app
COPY ${JAR_FILE} app.jar

# run
ENTRYPOINT ["java","-jar","/app.jar"]
