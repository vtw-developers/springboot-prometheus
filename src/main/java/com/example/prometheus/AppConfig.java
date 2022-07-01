package com.example.prometheus;

import com.codahale.metrics.MetricRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.ServiceStatus;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicyFactory;
import org.apache.camel.component.micrometer.MicrometerConstants;
import org.apache.camel.component.micrometer.messagehistory.MicrometerMessageHistoryFactory;
import org.apache.camel.component.micrometer.routepolicy.MicrometerRoutePolicyFactory;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public CamelContextConfiguration camelContextConfiguration(MeterRegistry meterRegistry) {

        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext camelContext) {
                camelContext.addRoutePolicyFactory(new MicrometerRoutePolicyFactory());
                camelContext.setMessageHistoryFactory(new MicrometerMessageHistoryFactory());
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                meterRegistry.gauge("camel_route_count", camelContext.getRoutesSize());
                for (Route route : camelContext.getRoutes()) {
                    ServiceStatus routeStatus = camelContext.getRouteController().getRouteStatus(route.getId());
                    meterRegistry.gauge("camel_route_status", List.of(Tag.of("routeId", route.getId())), routeStatus.ordinal());
                }
            }
        };
    }

//    @Bean(MicrometerConstants.METRICS_REGISTRY_NAME)
//    public PrometheusMeterRegistry meterRegistry() {
//        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
//    }

}