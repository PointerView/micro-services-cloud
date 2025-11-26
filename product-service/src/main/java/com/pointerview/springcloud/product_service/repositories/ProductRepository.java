package com.pointerview.springcloud.product_service.repositories;

import com.pointerview.springcloud.product_service.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
