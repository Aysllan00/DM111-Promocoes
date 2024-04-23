package br.inatel.dm111.persistence.promocao;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

//@Component
public class PromocaoMemoryRepository implements PromocaoRepository {

    private Set<Promocao> db = new HashSet<>();

    @Override
    public void save(Promocao promocao) {
        db.add(promocao);
    }

    @Override
    public List<Promocao> findAllByUserId(String userId) {
        return db.stream()
                .filter(p -> p.getId().equals(userId))
                .toList();
    }

    @Override
    public List<Promocao> findAll() throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Promocao findByUserId(String userId) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Optional<Promocao> findByUserIdAndId(String userId, String id) {
        return db.stream()
                .filter(p -> p.getId().equals(id))
                .filter(p -> p.getId().equals(userId))
                .findFirst();
    }

    @Override
    public void delete(String id) {
        db.removeIf(p -> p.getId().equals(id));
    }

    @Override
    public Promocao update(Promocao promocao) {
        delete(promocao.getId());
        save(promocao);
        return promocao;
    }
}