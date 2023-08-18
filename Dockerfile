FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY ./pom.xml /app
COPY ./jenjetsu-eureka-server /app/jenjetsu-eureka-server/
RUN mvn -f /app/jenjetsu-eureka-server/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/jenjetsu-eureka-server/target/*.jar /app/jenjetsu-eureka-server/*.jar
EXPOSE 8761
ENTRYPOINT ["java",  "-jar", "/app/jenjetsu-eureka-server/*.jar"]