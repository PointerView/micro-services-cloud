package com.pointerview.springcloud.product_service.services;


import com.pointerview.springcloud.common_service.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();
    Optional<Product> findById(Long id);
    Product save(Product product);
    void deleteById(Long id);
}
