FROM maven:3.8.1-openjdk-17-slim as builder
RUN mkdir /app
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests -Dmaven.wagon.http.ssl.insecure=true
RUN mv $(ls -1d /app/target/consorcio-api-*.jar) /app/target/consorcio-api.jar
ENV ACTIVE_PROFILE=dev

FROM openjdk:17-alpine
RUN mkdir /app
WORKDIR /app
COPY --from=builder /app/target/consorcio-api.jar .
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${ACTIVE_PROFILE}", "consorcio-api.jar"]