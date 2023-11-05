FROM maven:3.8.4-openjdk-17-slim AS builder
COPY ./BRT/target/*.jar app.jar
RUN mkdir -p app/extract && (cd app/extract; jar -xf /app.jar)

FROM openjdk:17-slim
VOLUME /tmp
ARG DEPENDENCY=/app/extract
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app
RUN apt-get -y update
RUN apt-get -y install curl
RUN apt-get -y install jq
RUN apt-get -y install nano
EXPOSE 8200
ENTRYPOINT ["java",  "-cp", "app:app/lib/*", "org.jenjetsu.com.brt.JenjetsuBrtMain"]
