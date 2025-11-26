package com.pointerview.springcloud.item_service.services;

import com.pointerview.springcloud.item_service.models.Item;
import com.pointerview.springcloud.item_service.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Primary
@Service
public class ProductServiceWebClient implements ItemService {

    @Autowired
    private WebClient.Builder webClient;

    @Override
    public List<Item> findAll() {
        return webClient.build()
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Product.class)
                .map(product -> new Item(product, new Random().nextInt(10)+1))
                .collectList()
                .block();
    }

    @Override
    public Optional<Item> findById(Long id) {

        try{
            return Optional.ofNullable(
                    webClient.build()
                            .get()
                            .uri("/{id}", Collections.singletonMap("id", id))
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(Product.class)
                            .map(product -> new Item(product, new Random().nextInt(10)+1))
                            .block());
        } catch (WebClientResponseException e) {
            return Optional.empty();
        }
    }
}
