package com.saasplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
    QuartzAutoConfiguration.class
})
@EnableScheduling
public class SaasPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaasPlatformApplication.class, args);
    }
}

