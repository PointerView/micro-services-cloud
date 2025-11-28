package com.pointerview.springcloud.item_service.services;

import com.pointerview.springcloud.common_service.entities.Product;
import com.pointerview.springcloud.item_service.clients.ProductFeignClient;
import com.pointerview.springcloud.item_service.models.Item;
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

    @Override
    public Product saveOne(Product product) {
        return productFeignClient.saveOne(product);
    }

    @Override
    public Product updateOne(Product product, Long id) {
        return productFeignClient.updateOne(product, id);
    }

    @Override
    public void deleteOneById(Long id) {
        productFeignClient.deleteOneById(id);
    }
}
