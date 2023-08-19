FROM maven:3.8.4-openjdk-17 AS builder
RUN mkdir -p /root/.m2/repository/org/jenjetsu/com/core/1.0-SNAPSHOT/
COPY ./Core/target/*.jar /root/.m2/repository/org/jenjetsu/com/core/1.0-SNAPSHOT/
WORKDIR /app
COPY ./pom.xml /app
COPY ./HRS /app/HRS/
RUN rm /app/HRS/src/main/resources/application.yml
RUN mv /app/HRS/src/main/resources/application-dep.yml /app/HRS/src/main/resources/application.yml
RUN mvn -f /app/HRS/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/HRS/target/*.jar /app/HRS/*.jar
EXPOSE 8102
ENTRYPOINT ["java",  "-jar", "/app/HRS/*.jar"]