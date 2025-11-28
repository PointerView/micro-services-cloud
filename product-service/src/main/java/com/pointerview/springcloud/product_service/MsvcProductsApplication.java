package com.pointerview.springcloud.product_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
/*Indica donde buscar entities fuera del proyecto, ya que la configuracion principal solo
* aplica a buscar dentro del proyecto en si y no dentro de dependencias externas*/
@EntityScan(basePackages = {"com.pointerview.springcloud.common_service.entities"})
public class MsvcProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcProductsApplication.class, args);
	}

}
