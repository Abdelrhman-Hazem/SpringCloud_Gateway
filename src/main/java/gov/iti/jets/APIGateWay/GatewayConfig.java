package gov.iti.jets.APIGateWay;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class GatewayConfig {

    @Autowired
    @Lazy
    private EurekaClient eurekaClient;
//    @Value("OrdersService")
    private String appName = "OrdersService";

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        InstanceInfo service = eurekaClient
                .getApplication(appName)
                .getInstances()
                .get(0);

        String hostName = service.getHostName();
        int port = service.getPort();

        return builder
                .routes()
                .route("example_route", r -> r.path("/orders/**").uri("http://"+hostName+":"+port))
                .build();
    }
}