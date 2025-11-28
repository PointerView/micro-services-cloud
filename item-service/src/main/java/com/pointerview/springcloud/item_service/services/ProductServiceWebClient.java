package com.pointerview.springcloud.item_service.services;

import com.pointerview.springcloud.common_service.entities.Product;
import com.pointerview.springcloud.item_service.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

//@Primary
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

        /*
        * Eliminado el try-catch para que retorne la excepcion y la reciba el run
        * del resilience4j y asi manejar el camino secundario
        * */
        //try{
            return Optional.ofNullable(
                    webClient.build()
                            .get()
                            .uri("/{id}", Collections.singletonMap("id", id))
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(Product.class)
                            .map(product -> new Item(product, new Random().nextInt(10)+1))
                            .block());
        //} catch (WebClientResponseException e) {
        //    return Optional.empty();
        //}
    }

    @Override
    public Product saveOne(Product product) {
        return webClient.build()
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(product)
            .retrieve()
            .bodyToMono(Product.class)
            .block();
    }

    @Override
    public Product updateOne(Product product, Long id) {
        return webClient.build()
            .put()
            .uri("/{id}", Collections.singletonMap("id", id))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(product)
            .retrieve()
            .bodyToMono(Product.class)
            .block();
    }

    @Override
    public void deleteOneById(Long id) {
        webClient.build()
            .delete()
            .uri("/{id}", Collections.singletonMap("id", id))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
