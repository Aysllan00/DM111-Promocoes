package br.inatel.dm111.persistence.promocao;

import br.inatel.dm111.persistence.product.Product;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface PromocaoRepository {

    void save(Promocao promocao);

    List<Promocao> findAllByUserId(String userId) throws ExecutionException, InterruptedException;

    List<Promocao> findAll() throws ExecutionException, InterruptedException;

    public Promocao findByUserId(String userId) throws ExecutionException, InterruptedException;

    Optional<Promocao> findByUserIdAndId(String userId, String id) throws ExecutionException, InterruptedException;

    void delete(String id) throws ExecutionException, InterruptedException;

    Promocao update(Promocao promocao);
}