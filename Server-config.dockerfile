FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /app
COPY ./pom.xml /app/
COPY ./jenjetsu-config /app/jenjetsu-config/
RUN mv -f /app/jenjetsu-config/src/main/resources/application-dep.yml /app/jenjetsu-config/src/main/resources/application.yml
RUN mvn -f /app/jenjetsu-config/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/jenjetsu-config/target/*.jar /app/jenjetsu-config/*.jar
EXPOSE 8888
ENTRYPOINT ["java",  "-jar", "/app/jenjetsu-config/*.jar"]