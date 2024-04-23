package br.inatel.dm111.persistence.user;

import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class UserFirebaseRepository {

    private static final String COLLECTION_NAME = "users";

    private final Firestore firestore;

    public UserFirebaseRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public void save(User user) {
        firestore.collection(COLLECTION_NAME)
                .document(user.getId())
                .set(user);
    }

    public List<User> findAll() throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .parallelStream()
                .map(user -> user.toObject(User.class))
                .toList();
    }

    public Optional<User> findById(String id) throws ExecutionException, InterruptedException {
        var user = firestore.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .get()
                .toObject(User.class);

        return Optional.ofNullable(user);
    }

    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    public void update(User user) {
        save(user);
    }

    public Optional<User> findByEmail(String email) throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(user -> user.toObject(User.class))
                .filter(user -> user.getEmail().toLowerCase().equals(email.toLowerCase()))
                .findFirst();
    }
}
