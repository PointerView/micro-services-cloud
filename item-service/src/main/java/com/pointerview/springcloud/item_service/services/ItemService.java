package com.pointerview.springcloud.item_service.services;

import com.pointerview.springcloud.common_service.entities.Product;
import com.pointerview.springcloud.item_service.models.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    List<Item> findAll();
    Optional<Item> findById(Long id);
    Product saveOne(Product product);
    Product updateOne(Product product, Long id);
    void deleteOneById(Long id);
}
