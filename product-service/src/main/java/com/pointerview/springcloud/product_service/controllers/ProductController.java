package com.pointerview.springcloud.product_service.controllers;


import com.pointerview.springcloud.product_service.entities.Product;
import com.pointerview.springcloud.product_service.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> details(@PathVariable Long id) {
        Optional<Product> optionalProduct = productService.findById(id);

        if(optionalProduct.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(optionalProduct.orElseThrow());
    }
}
