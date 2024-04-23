package br.inatel.dm111.api.product.controller;

import br.inatel.dm111.api.core.ApiException;
import br.inatel.dm111.api.product.ProductRequest;
import br.inatel.dm111.api.product.service.ProductService;
import br.inatel.dm111.persistence.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//http://localhost:8080/dm111/products
@RestController
@RequestMapping("/dm111")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() throws ApiException {
        log.debug("Getting all products");
        var produts = service.searchProducts();
        return ResponseEntity.ok(produts);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id) throws ApiException {
        log.debug("Getting the product by id: " + id);
        var product = service.searchProduct(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> postProduct(@RequestBody ProductRequest request) {
        var product = service.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> putProduct(@PathVariable("id") String id,
                                              @RequestBody ProductRequest request) throws ApiException {
        var product = service.updateProduct(id, request);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id")String id) throws ApiException {
        service.removeProduct(id);
        return ResponseEntity.noContent().build();
    }
}
