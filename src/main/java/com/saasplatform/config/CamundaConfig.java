package com.saasplatform.config;

import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamundaConfig {

    /**
     * Disable Camunda health indicators to prevent startup issues
     */
    @Bean
    @ConditionalOnProperty(name = "management.health.camunda.enabled", havingValue = "false", matchIfMissing = true)
    public HealthIndicator camundaHealthIndicator() {
        return () -> org.springframework.boot.actuate.health.Health.up()
                .withDetail("message", "Camunda health check disabled")
                .build();
    }
}






