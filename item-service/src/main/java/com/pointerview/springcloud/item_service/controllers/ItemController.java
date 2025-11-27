package com.pointerview.springcloud.item_service.controllers;

import com.pointerview.springcloud.item_service.models.Item;
import com.pointerview.springcloud.item_service.services.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public ResponseEntity<List<Item>> findAll(@RequestParam(required = false) String name,
                                              @RequestHeader(name = "token-request") String token) {

        System.out.println(name);
        System.out.println(token);
        return ResponseEntity.ok(itemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Item> itemOptional = itemService.findById(id);

        return itemOptional.isPresent()
                ? ResponseEntity.ok(itemOptional.orElseThrow())
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "This product doesn't exist in this item-service"));
    }
}
