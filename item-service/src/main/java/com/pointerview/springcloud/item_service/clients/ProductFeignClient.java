package com.pointerview.springcloud.item_service.clients;

import com.pointerview.springcloud.common_service.entities.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

    @GetMapping
    List<Product> findAll();

    @GetMapping("/{id}")
    Product findById(@PathVariable Long id);

    @PostMapping
    Product saveOne(@RequestBody Product product);

    @PutMapping("/{id}")
    Product updateOne(@RequestBody Product product, @PathVariable Long id);

    @DeleteMapping("/{id}")
    void deleteOneById(@PathVariable Long id);
}
