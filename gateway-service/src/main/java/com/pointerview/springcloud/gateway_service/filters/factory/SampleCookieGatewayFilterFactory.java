package com.pointerview.springcloud.gateway_service.filters.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class SampleCookieGatewayFilterFactory extends AbstractGatewayFilterFactory<SampleCookieGatewayFilterFactory.ConfigurationCookie> {

    private final Logger log = LoggerFactory.getLogger(SampleCookieGatewayFilterFactory.class);

    public SampleCookieGatewayFilterFactory() {
        super(ConfigurationCookie.class);
    }

    @Override
    public GatewayFilter apply(SampleCookieGatewayFilterFactory.ConfigurationCookie config) {
        /*
        * Envolver el valor del return en un OrderedGatewayFilter que tiene como primer argumento
        * la BiFunction y como segundo el orden de ejecucion del filtro.
        */
        return ((exchange, chain) -> {
            log.info("Ejecutando pre gateway filter factory: " + config.message);
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if(config.getName() != null)
                    exchange.getResponse().addCookie(ResponseCookie.from(config.getName(), config.getValue()).build());

                log.info("Ejecutando post gateway filter factory: " + config.message);
            }));
        });
    }

    @Override
    public String name() {
        return "SampleCookie";
    }

    public static class ConfigurationCookie {

        /*
        * Clase que almacena los valores parametrizados desde el yml los cuales se agregan usando el nombre
        * del atributo, pero implícitamente llaman al setter
        */
        private String name;
        private String value;
        private String message;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
