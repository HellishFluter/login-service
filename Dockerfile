FROM maven:3-jdk-11 as builder
WORKDIR /app
COPY pom.xml ./
RUN mvn -B dependency:resolve dependency:resolve-plugins
COPY src ./src/
RUN mvn clean package -Dmaven.test.skip=true

FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=app/target/*.jar
COPY --from=builder ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]