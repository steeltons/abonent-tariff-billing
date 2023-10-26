FROM maven:3.8.4-openjdk-17-slim AS builder
RUN mkdir -p /root/.m2/repository/org/jenjetsu/com/core/1.0-SNAPSHOT/
COPY ./Core/target/*.jar /root/.m2/repository/org/jenjetsu/com/core/1.0-SNAPSHOT/
WORKDIR /app
COPY ./pom.xml /app/
COPY ./BRT /app/BRT/
RUN rm /app/BRT/src/main/resources/application.yml
RUN mv /app/BRT/src/main/resources/application-dev.yml /app/BRT/src/main/resources/application.yml
RUN mvn -f /app/BRT/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/BRT/target/*.jar /app/BRT/*.jar
EXPOSE 8102
ENTRYPOINT ["java",  "-jar", "/app/BRT/*.jar"]
