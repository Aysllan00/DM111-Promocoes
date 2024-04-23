package br.inatel.dm111.persistence.promocao;

import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class PromocaoFirebaseRepository implements PromocaoRepository {

    private static final String COLLECTION_NAME = "promocoes";

    private final Firestore firestore;

    public PromocaoFirebaseRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void save(Promocao promocao) {
        firestore.collection(COLLECTION_NAME)
                .document(promocao.getId())
                .set(promocao);
    }

    @Override
    public List<Promocao> findAllByUserId(String userId) throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .parallelStream()
                .map(document -> document.toObject(Promocao.class))
                .filter(promocao -> promocao.getId().equals(userId))
                .toList();
    }

    @Override
    public List<Promocao> findAll() throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .parallelStream()
                .map(product -> product.toObject(Promocao.class))
                .toList();
    }

    @Override
    public Optional<Promocao> findByUserIdAndId(String userId, String id) throws ExecutionException, InterruptedException {
        var promocao = firestore.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .get()
                .toObject(Promocao.class);

        if (promocao != null && promocao.getId().equals(userId)) {
            return Optional.of(promocao);
        }

        return Optional.empty();
    }

    @Override
    public Promocao findByUserId(String userId) throws ExecutionException, InterruptedException {
        var promocoes = firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .parallelStream()
                .map(document -> document.toObject(Promocao.class))
                .filter(promocao -> promocao.getId().equals(userId))
                .toList();

        if (!promocoes.isEmpty()) {
            return promocoes.get(0);
        } else {
            return null; // ou lançar uma exceção indicando que a promoção não foi encontrada
        }
    }


    @Override
    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    @Override
    public Promocao update(Promocao promocao) {
        save(promocao);
        return promocao;
    }
}
