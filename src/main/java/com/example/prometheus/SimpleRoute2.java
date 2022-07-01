package com.example.prometheus;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleRoute2 extends EndpointRouteBuilder {

    @Override
    public void configure() throws Exception {
        from(direct("test"))
                .log("Pgogo");
    }

}
