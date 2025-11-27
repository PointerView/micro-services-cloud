package com.pointerview.springcloud.gateway_service.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class SampleGlobalFilter implements GlobalFilter, Ordered {
    /*
    * Creación de clase derivada de GlobalFilter declarada como componente haciendo
    * que sea un filtro global aplicado a todas las request.
    *
    * También se hace uso de Ordered para darle orden dentro de la filter chain
    * */

    private final Logger log = LoggerFactory.getLogger(SampleGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info("Ejecutando el filtro antes del request PRE");

        ServerHttpRequest modifiedRequest = exchange.getRequest()
                .mutate()
                .header("token", "abcdefg")
                .build();

        ServerWebExchange modifiedExchange = exchange.mutate()
                .request(modifiedRequest)
                .build();

        return chain.filter(modifiedExchange).then(Mono.fromRunnable(() -> {
            /*Al ser un metodo asincrono se debe de manejar de ese modo mediante then
            * para que se ejecute solo cuando el filter haya finalizado*/
            log.info("Ejecutando el filtro después del request POST");
            String token = modifiedExchange.getRequest().getHeaders().get("token").getFirst();

            if(token != null) {
                log.info("token: " + token);
                modifiedExchange.getResponse().getHeaders().add("token", token);
            }
            modifiedExchange.getResponse().addCookie(ResponseCookie.from("color", "red").build());
            modifiedExchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        }));
    }

    @Override
    public int getOrder() {
        return 100; /*A partir de 100 para no afectar el orden de los filtros
        internos de spring*/
    }
}
