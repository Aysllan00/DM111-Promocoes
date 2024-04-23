package br.inatel.dm111.persistence.supermarketlist;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

//@Component
public class SuperMarketListMemoryRepository implements SuperMarketListRepository{

    private Set<SuperMarketList> db = new HashSet<>();

    @Override
    public void save(SuperMarketList superMarketList) {
        db.add(superMarketList);
    }

    @Override
    public List<SuperMarketList> findAllByUserId(String userId) {
        return db.stream()
                .filter(spl -> spl.getUserId().equals(userId))
                .toList();
    }

    @Override
    public Optional<SuperMarketList> findByUserIdAndId(String userId, String id) {
        return db.stream()
                .filter(spl -> spl.getId().equals(id))
                .filter(spl -> spl.getUserId().equals(userId))
                .findFirst();
    }


    @Override
    public void delete(String id) {
        db.removeIf(spl -> spl.getId().equals(id));
    }

    @Override
    public void update(SuperMarketList superMarketList) {
        delete(superMarketList.getId());
        save(superMarketList);
    }
}
