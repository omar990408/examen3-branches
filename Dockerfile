FROM eclipse-temurin:17-jdk-focal
LABEL authors="Omar"

COPY  target/branches-0.1.jar branches-0.1.jar
ENTRYPOINT ["java","-jar","/branches-0.1.jar"]
EXPOSE 8090