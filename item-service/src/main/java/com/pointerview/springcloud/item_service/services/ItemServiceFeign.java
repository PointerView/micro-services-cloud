package com.pointerview.springcloud.item_service.services;

import com.pointerview.springcloud.item_service.clients.ProductFeignClient;
import com.pointerview.springcloud.item_service.models.Item;
import com.pointerview.springcloud.item_service.models.Product;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ItemServiceFeign implements ItemService{

    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public List<Item> findAll() {
        return productFeignClient.findAll().stream()
                .map(product -> new Item(product, new Random().nextInt(10) + 1))
                .toList();
    }

    @Override
    public Optional<Item> findById(Long id) {

        try{
            Product product = productFeignClient.findById(id);
            return Optional.of(new Item(product, new Random().nextInt(10) + 1));
        } catch (FeignException e) {
            return Optional.empty();
        }
    }
}
