package com.pointerview.springcloud.item_service.controllers;

import com.pointerview.springcloud.common_service.entities.Product;
import com.pointerview.springcloud.item_service.models.Item;
import com.pointerview.springcloud.item_service.services.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RefreshScope // Para refrescar el componente con el servidor activo
@RestController
public class ItemController {

    @Autowired
    @Qualifier("itemServiceFeign")
    private ItemService itemService;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Value("${configuracion.texto}")
    private String text;

    @Value("${server.port}")
    private String port;

    @Autowired
    private Environment env; // Alternativa a @Value()

    private final Logger log = LoggerFactory.getLogger(ItemController.class);

    @GetMapping
    public ResponseEntity<List<Item>> findAll(@RequestParam(required = false) String name,
                                              @RequestHeader(name = "token-request") String token) {
        log.info("Llamada a metodo del controller ItemController::list()");
        log.info("Token: {}", token);
        log.info("Request Parameter: {}", text);
        return ResponseEntity.ok(itemService.findAll());
    }

    @GetMapping("/fetch-config")
    public ResponseEntity<?> fetchConfigs() {
        Map<String, String> output = new HashMap<>();
        output.put("port", port);
        output.put("text", text);
        log.info(text);
        log.info(port);

        if(env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")){
            output.put("autor.nombre", env.getProperty("configuracion.autor.name"));
            output.put("autor.email", env.getProperty("configuracion.autor.email"));
        }

        return ResponseEntity.ok(output);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Item> itemOptional = circuitBreakerFactory.create("items").run(() -> itemService.findById(id),
                e -> {
                    log.error(e.getMessage());

//                    Product product = Product.builder()
//                            .createAt(LocalDate.now())
//                            .id(1L)
//                            .name("Camara Sony")
//                            .price(500D)
//                            .build();

                    Product product = new Product();
                    product.setCreateAt(LocalDate.now());
                    product.setId(1L);
                    product.setName("Camara Sony");
                    product.setPrice(500D);

                    return Optional.of(new Item(product, 5));
                });

        return itemOptional.isPresent()
                ? ResponseEntity.ok(itemOptional.orElseThrow())
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "This product doesn't exist in this item-service"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        log.info("Product creando: {}", product);
        return itemService.saveOne(product);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Product update(@RequestBody Product product, @PathVariable Long id) {
        log.info("Product actualizando: {}", product);
        return itemService.updateOne(product, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("Product eliminado con id: {}", id);
        itemService.deleteOneById(id);
    }
}
