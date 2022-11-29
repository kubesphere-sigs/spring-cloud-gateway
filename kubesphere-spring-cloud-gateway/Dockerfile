FROM openjdk:8-alpine3.9
WORKDIR /app
COPY target/kubesphere-spring-cloud-gateway.jar /app

ENV TZ Asia/Shanghai
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "kubesphere-spring-cloud-gateway.jar"]
