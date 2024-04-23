package br.inatel.dm111.persistence.product;

import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class ProductFirebaseRepository implements ProductRepository{

    private static final String COLLECTION_NAME = "products";

    private final Firestore firestore;

    public ProductFirebaseRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void save(Product product) {
        firestore.collection(COLLECTION_NAME)
                .document(product.getId())
                .set(product);
    }

    @Override
    public List<Product> findAll() throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .parallelStream()
                .map(product -> product.toObject(Product.class))
                .toList();
    }

    @Override
    public Optional<Product> findById(String id) throws ExecutionException, InterruptedException {
        var product = firestore.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .get()
                .toObject(Product.class);
        return Optional.ofNullable(product);
    }

    @Override
    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    @Override
    public void update(Product product) {
       save(product);
    }
}
