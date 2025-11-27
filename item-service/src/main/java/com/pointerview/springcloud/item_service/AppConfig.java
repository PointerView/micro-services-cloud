package com.pointerview.springcloud.item_service;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AppConfig {

    /*
    * Creación de FactoryBean de customizerCircuitBreaker de forma programática, también
    * configurado en los properties del application.yml la cual tiene preferencia sobre
    * la configuración programática por lo que estas configuraciones serán sobrescritras
    * por las del application.yml
    * */
    @Bean
    Customizer<Resilience4JCircuitBreakerFactory> customizerCircuitBreaker() {
        return factory -> factory.configureDefault(circuitBreakerId -> {
            return new Resilience4JConfigBuilder(circuitBreakerId) // Id puesto al circuit breaker cuando es creado
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                    .slidingWindowSize(10) // Número de peticiones a medir en opened
                    .failureRateThreshold(50) // Porcentaje de fallos para pasar de closed a opened
                    .waitDurationInOpenState(Duration.ofSeconds(10L)) // Tiempo en estado abierto
                    .permittedNumberOfCallsInHalfOpenState(5) // numero de peticiones a medir en half opened
                    .slowCallDurationThreshold(Duration.ofSeconds(2L))
                    .slowCallRateThreshold(50)
                    .build())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                    .timeoutDuration(Duration.ofSeconds(4L))
                    .build())
                .build();
        });
    }
}

/*
* La diferencia entre un timeout y una slow call es que la primera retorna una excepcion generando que se finalice
* la request y entre en el camino alternativo mientras que las slow calls ni finalizan la request ni retorna una
* excepcion por lo que no ejecuta el camino alternativo, solo el principal, pero ambos pueden abrir el cortocircuito
* */