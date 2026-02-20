package com.marcello.cupons.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Clock como bean:
 * - permite testar data sem depender de “hoje”
 * - facilita controlar tempo em testes/integracao
 */
@Configuration
public class ClockConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
