package com.pointerview.springcloud.product_service.controllers;


import com.pointerview.springcloud.common_service.entities.Product;
import com.pointerview.springcloud.product_service.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
public class ProductController {

    final ProductService productService;

    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> list() {
        log.info("Ingresando al metodo del controller ProductController:list");
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> details(@PathVariable Long id) throws InterruptedException {
        log.info("Ingresando al metodo del controller ProductController:details");

        if(id.equals(10L)) throw new IllegalStateException("Producto no encontrado");

        if(id.equals(7L)) TimeUnit.SECONDS.sleep(3L);


        Optional<Product> optionalProduct = productService.findById(id);

        if(optionalProduct.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(optionalProduct.orElseThrow());
    }

    @PostMapping
    public ResponseEntity<Product> saveOne(@RequestBody Product product) {
        log.info("Ingresando al metodo ProductController:saveOne creando: {}", product);

        Product p = productService.save(product);

        if(p == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateOne(@PathVariable Long id, @RequestBody Product product) {
        log.info("Ingresando al metodo ProductController::updateOne actualizando: {}", product);

        Optional<Product> optionalProduct = productService.findById(id);
        if(optionalProduct.isEmpty()) return ResponseEntity.notFound().build();

        Product responseProduct = optionalProduct.orElseThrow();
        responseProduct.setPort(product.getPort());
        responseProduct.setName(product.getName());
        responseProduct.setPrice(product.getPrice());
        responseProduct.setCreateAt(product.getCreateAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(responseProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneById(@PathVariable Long id) {
        Optional<Product> optionalProduct = productService.findById(id);
        if(optionalProduct.isEmpty()) return ResponseEntity.notFound().build();

        log.info("Ingresando al metodo ProductController:deleteOneById, eliminado: {}", optionalProduct.orElseThrow());

        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
