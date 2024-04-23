package br.inatel.dm111.persistence.product;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

//@Component
public class ProductMemoryRepository implements ProductRepository {

    private Set<Product> db = new HashSet<>();

    @Override
    public void save(Product product) {
        db.add(product);
    }

    @Override
    public void update(Product product) {
        delete(product.getId());
        save(product);
    }

    @Override
    public List<Product> findAll() {
        return db.stream().toList();
    }

    @Override
    public Optional<Product> findById(String id) {
        return db.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    @Override
    public void delete(String id) {
        db.removeIf(p -> p.getId().equals(id));
    }
}
