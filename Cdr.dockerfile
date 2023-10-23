FROM maven:3.8.4-openjdk-17 as builder
RUN mkdir -p /root/.m2/repository/org/jenjetsu/com/core/1.0-SNAPSHOT/
COPY ./Core/target/*.jar /root/.m2/repository/org/jenjetsu/com/core/1.0-SNAPSHOT/
WORKDIR /app
COPY ./pom.xml /app
COPY ./CDR /app/CDR/
RUN mvn -f /app/CDR/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/CDR/target/*.jar /app/CDR/*.jar
EXPOSE 8100
ENV JAVA_OPTS="-Dspring.profile.active=dep"
ENTRYPOINT ["java",  "-jar", "/app/CDR/*.jar"]
