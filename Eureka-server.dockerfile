FROM openjdk:17-slim AS build
COPY ./jenjetsu-eureka-server/target/*.jar app.jar
RUN mkdir -p app/extract && (cd app/extract; jar -xf /app.jar)
RUN rm app/extract/BOOT-INF/classes/bootstrap.yml
RUN mv app/extract/BOOT-INF/classes/bootstrap-prod.yml app/extract/BOOT-INF/classes/bootstrap.yml

FROM openjdk:17-slim
VOLUME /tmp
ARG DEPENDENCY=/app/extract
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
RUN apt-get -y update
RUN apt-get -y install curl
RUN apt-get -y install jq
RUN apt-get -y install nano
EXPOSE 8761
ENTRYPOINT ["java",  "-cp", "app:app/lib/*", "org.jenjetsu.com.eureka.JenjetsuEurekaMain"]