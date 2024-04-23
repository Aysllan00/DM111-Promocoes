package br.inatel.dm111.api.product.service;

import br.inatel.dm111.api.core.ApiException;
import br.inatel.dm111.api.core.AppErrorCode;
import br.inatel.dm111.api.product.ProductRequest;
import br.inatel.dm111.persistence.product.Product;
import br.inatel.dm111.persistence.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> searchProducts() throws ApiException {
        try {
            return repository.findAll();
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
        }
    }

    public Product searchProduct(String id) throws ApiException {
        return retrieveProduct(id);
    }

    public Product createProduct(ProductRequest request) {
        var product = buildProduct(request);
        repository.save(product);
        return product;
    }

    public Product updateProduct(String id, ProductRequest request) throws ApiException {
        var product = retrieveProduct(id);
        product.setName(request.name());
        product.setAmount(request.amount());
        product.setBrand(request.brand());
        product.setUnit(request.unit());
        product.setPrice(request.price());

        repository.update(product);
        return product;
    }

    public void removeProduct(String id) throws ApiException {
        try {
            repository.delete(id);
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
        }
    }

    private Product retrieveProduct(String id) throws ApiException {
        try {
            return repository.findById(id)
                    .orElseThrow(() -> new ApiException(AppErrorCode.PRODUCT_NOT_FOUND));
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
        }
    }

    private Product buildProduct(ProductRequest request) {
        var id = UUID.randomUUID().toString();
        return new Product(id,
                request.name(),
                request.brand(),
                request.unit(),
                request.amount(),
                request.price());
    }
}
