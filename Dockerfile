FROM gradle:7.1-jdk8 AS builder
ARG TARGET=gateway
COPY / /app/builder
WORKDIR /app/builder
RUN gradle build

FROM openjdk:8-alpine3.9
ARG TARGET=gateway
COPY --from=builder /app/builder/build/libs/ /app
WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]