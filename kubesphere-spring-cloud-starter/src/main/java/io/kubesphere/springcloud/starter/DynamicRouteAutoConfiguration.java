package io.kubesphere.springcloud.starter;

import io.kubesphere.springcloud.DynamicRouteController;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * io.kubesphere.springcloud.starter.DynamicRouteAutoConfiguration
 *
 * @author hongzhouzi
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({GatewayAutoConfiguration.class})
public class DynamicRouteAutoConfiguration {

    @Bean
    protected DynamicRouteController dynamicRouteController(RouteDefinitionWriter routeDefinitionWriter) {
        return new DynamicRouteController(routeDefinitionWriter);
    }
}
