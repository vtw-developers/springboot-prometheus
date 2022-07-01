package com.example.prometheus;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleRoute extends EndpointRouteBuilder {

    @Override
    public void configure() throws Exception {
        from(timer("test"))
                .log("Pgogo");
    }

}
