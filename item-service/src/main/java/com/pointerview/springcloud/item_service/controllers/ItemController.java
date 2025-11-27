package com.pointerview.springcloud.item_service.controllers;

import com.pointerview.springcloud.item_service.models.Item;
import com.pointerview.springcloud.item_service.models.Product;
import com.pointerview.springcloud.item_service.services.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    private final Logger log = LoggerFactory.getLogger(ItemController.class);

    @GetMapping
    public ResponseEntity<List<Item>> findAll(@RequestParam(required = false) String name,
                                              @RequestHeader(name = "token-request") String token) {

        System.out.println(name);
        System.out.println(token);
        return ResponseEntity.ok(itemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Item> itemOptional = circuitBreakerFactory.create("items").run(() -> itemService.findById(id),
                e -> {
                    log.error(e.getMessage());

                    Product product = Product.builder()
                            .createAt(LocalDate.now())
                            .id(1L)
                            .name("Camara Sony")
                            .price(500D)
                            .build();

                    return Optional.of(new Item(product, 5));
                });

        return itemOptional.isPresent()
                ? ResponseEntity.ok(itemOptional.orElseThrow())
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "This product doesn't exist in this item-service"));
    }
}
