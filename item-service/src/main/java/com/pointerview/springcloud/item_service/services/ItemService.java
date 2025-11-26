package com.pointerview.springcloud.item_service.services;

import com.pointerview.springcloud.item_service.models.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    List<Item> findAll();
    Optional<Item> findById(Long id);
}
