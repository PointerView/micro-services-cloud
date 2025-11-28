package com.pointerview.springcloud.common_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class}) /*Excluye esta configuracion
para que no lo pida al usar esta dependencia en otros proyectos donde solo necesitemos las clases
como DTOs*/
public class CommonServiceApplication {

}
