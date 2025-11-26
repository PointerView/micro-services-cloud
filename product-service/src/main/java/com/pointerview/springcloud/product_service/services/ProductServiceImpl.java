package com.pointerview.springcloud.product_service.services;

import com.pointerview.springcloud.product_service.entities.Product;
import com.pointerview.springcloud.product_service.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    private final Environment env;

    public ProductServiceImpl(ProductRepository productRepository, Environment env) {
        this.productRepository = productRepository;
        this.env = env;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll().stream()
                .peek(product -> {
                    product.setPort(Integer.valueOf(env.getProperty("local.server.port")));
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id)
                .map(p -> {
                    p.setPort(Integer.parseInt(env.getProperty("local.server.port")));
                    return p;
                });
    }
}
