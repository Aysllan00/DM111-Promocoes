package br.inatel.dm111.persistence.product;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface ProductRepository {

    void save(Product product);

    List<Product> findAll() throws ExecutionException, InterruptedException;

    Optional<Product> findById(String id) throws ExecutionException, InterruptedException;

    void delete(String id) throws ExecutionException, InterruptedException;

    void update(Product product);
}
