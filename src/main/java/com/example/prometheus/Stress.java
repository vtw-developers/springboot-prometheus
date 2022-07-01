package com.example.prometheus;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class Stress {

    @PostConstruct
    public void start() {
        for (int j = 0; j < 10; j++) {
            new Thread(() -> {
                for (int i = 0; ; i++) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }

}
