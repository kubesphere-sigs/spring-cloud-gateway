FROM openjdk:8-alpine3.9
WORKDIR /app
COPY ./target/kubesphere-spring-cloud-gateway.jar /app
ENTRYPOINT ["java", "-jar", "kubesphere-spring-cloud-gateway.jar"]
