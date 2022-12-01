# spring-cloud-gateway
简化在 Kubernetes 上接入 Spring Cloud Gateway，支持网关路由的动态配置及分发。


## 自定义网关镜像
自定义网关镜像可以通过以下两种方式：

1. 在源代码的基础上重新构建 kubesphere-spring-cloud-gateway
2. 基于提供的 kubesphere-spring-cloud-gateway-starter 集成

以下是基于 kubesphere-spring-cloud-gateway-starter 拓展示例。

### 示例
下面是一个自定义扩展的简单例子，它在发送到目标服务的请求中添加了一个HTTP头。

自定义 Filter 示例代码
1.创建项目，引入 starter 依赖
```xml
<dependency>
    <groupId>io.kubesphere</groupId>
    <artifactId>kubesphere-spring-cloud-starter</artifactId>
    <version>${kubesphere-gateway.version}</version>
</dependency>
```
2.创建自定义 Filter
```java
package io.kubesphere.springcloud.extensions.filter;
 
import io.kubesphere.springcloud.DynamicRouteController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
 
import java.util.Collections;
 
@Component
public class AddMyCustomHeaderGatewayFilterFactory
        extends AbstractGatewayFilterFactory<Object> {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(AddMyCustomHeaderGatewayFilterFactory.class);
 
    private static final String MY_HEADER_KEY = "X-My-Header";
 
    @Autowired
    DynamicRouteController dynamicRouteController;
 
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) ->
        {
            ServerWebExchange updatedExchange
                    = exchange.mutate()
                    .request(request -> {
                        request.headers(headers -> {
                            headers.put(MY_HEADER_KEY, Collections.singletonList("my-header-value"));
                            LOGGER.info("Processed request, added" + MY_HEADER_KEY + " header");
                        });
                    })
                    .build();
            return chain.filter(updatedExchange);
        };
    }
}
```

3.若没有自动创建 springboot 启动文件，就手动创建下
```java
package io.kubesphere.springcloud.extensions;
 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
 
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```
4.写 Dockerfile
```dockerfile
FROM openjdk:8-alpine3.9
WORKDIR /app
COPY ./target/xxx.jar /app
ENTRYPOINT ["java", "-jar", "xxx.jar"]
```
5.可以在 resources 目录下创建 bootstrap 配置文件，在里面开启 management.endpoints.web.exposure.include=*
```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```
然后就打 jar 包，打 docker 镜像，将镜像推送到镜像仓库。
打 jar 包前注意检查是否添加了如下插件：
```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
      <executions>
        <execution>
          <goals>
            <goal>repackage</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```

部署自定义网关后测试
在网关路由中添加如下网关路由
```yaml
- uri: http://localhost:8080
  predicates:
    - Path=/add-header/**
  filters:
    - StripPrefix=0
    - AddMyCustomHeader
```

如下请求：

curl ${ip}:${port}/add-header
然后在网关日志中可以看到 Processed request, addedX-My-Header header ，说明刚刚添加到过滤器已经生效了。




