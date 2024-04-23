package br.inatel.dm111.persistence.supermarketlist;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface SuperMarketListRepository {

    void save(SuperMarketList superMarketList);

    List<SuperMarketList> findAllByUserId(String userId) throws ExecutionException, InterruptedException;

    Optional<SuperMarketList> findByUserIdAndId(String userId, String id) throws ExecutionException, InterruptedException;

    // Método para encontrar as últimas listas de supermercado do usuário

    void delete(String id) throws ExecutionException, InterruptedException;

    void update(SuperMarketList superMarketList);
}
