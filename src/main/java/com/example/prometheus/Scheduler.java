package com.example.prometheus;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.ServiceStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

//@Component
public class Scheduler {

  private final AtomicInteger testGauge;
  private final Counter testCounter;
  //private final Map<String, Object> routeStatuses = new HashMap<>();

  public Scheduler(MeterRegistry meterRegistry, CamelContext context) {
    // Counter vs. gauge, summary vs. histogram
    // https://prometheus.io/docs/practices/instrumentation/#counter-vs-gauge-summary-vs-histogram



    Collection<Route> controlledRoutes = context.getRouteController().getControlledRoutes();

    meterRegistry.gauge("camel_route_count", context.getRoutesSize());

    for (Route route : context.getRoutes()) {
      ServiceStatus routeStatus = context.getRouteController().getRouteStatus(route.getId());
//      routeStatuses.put(route.getId(), routeStatus.name());

      meterRegistry.gauge("camel_route_status", List.of(Tag.of("routeId", route.getId())), routeStatus.ordinal());
    }

    testGauge = meterRegistry.gauge("custom_gauge", List.of(Tag.of("abc", "def")), new AtomicInteger(0));
    testCounter = meterRegistry.counter("custom_counter");

  }

  @Scheduled(fixedRateString = "1000", initialDelayString = "0")
  public void schedulingTask() {
    testGauge.set(Scheduler.getRandomNumberInRange(0, 100));

    testCounter.increment();
  }

  private static int getRandomNumberInRange(int min, int max) {
    if (min >= max) {
      throw new IllegalArgumentException("max must be greater than min");
    }

    Random r = new Random();
    return r.nextInt((max - min) + 1) + min;
  }
}