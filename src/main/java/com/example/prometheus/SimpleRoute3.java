package com.example.prometheus;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleRoute3 extends EndpointRouteBuilder {

    @Override
    public void configure() throws Exception {
        from(timer("kafka"))
        .setBody(constant("test"))
        .to(kafka("test-001"))
        .log("kafka");

        from(timer("kafka2").period(2000))
        .setBody(constant("test"))
        .to(kafka("test-002"))
        .log("kafka");

        from(timer("kafka3").period(3000))
        .setBody(constant("gogogogogogogogogogogogogo"))
                .log("eeee")
                .delay(50).syncDelayed()
                .log("ff")
                .to(kafka("test-002").advanced().synchronous(false))
                .log("fwfwf")
        .to(kafka("test-003").advanced().synchronous(true))
        .log("kafka");


        from(kafka("test-001").groupId("aaa").advanced().synchronous(true))
        .log("${body}");

        from(kafka("test-001").groupId("bbb"))
        .log("${body}");

        from(kafka("test-002").groupId("ccc"))
        .log("${body}");

        from(kafka("test-003").groupId("ddd").advanced().synchronous(true))
                .log("wwww")
                .delay(30).syncDelayed()
        .log("${body}")
                .to(kafka("test-004"));

        from(kafka("test-003").groupId("eee").advanced().synchronous(true))
                .log("ppp")
                .delay(20).syncDelayed()
        .log("${body}")
                .to(kafka("test-005"));
    }

}
