package br.inatel.dm111.api.supermaketlist.service;

import br.inatel.dm111.api.core.ApiException;
import br.inatel.dm111.api.core.AppErrorCode;
import br.inatel.dm111.api.supermaketlist.SuperMarketListRequest;
import br.inatel.dm111.persistence.product.ProductRepository;
import br.inatel.dm111.persistence.supermarketlist.SuperMarketList;
import br.inatel.dm111.persistence.supermarketlist.SuperMarketListRepository;
import br.inatel.dm111.persistence.user.UserFirebaseRepository;
import br.inatel.dm111.messaging.publisher.SuperMarketListPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class SuperMarketListService {

    private static final Logger log = LoggerFactory.getLogger(SuperMarketListService.class);

    private final SuperMarketListRepository splRepository;
    private final ProductRepository productRepository;
    private final UserFirebaseRepository userRepository;
    private final SuperMarketListPublisher publisher;

    public SuperMarketListService(SuperMarketListRepository splRepository, ProductRepository productRepository, UserFirebaseRepository userRepository, SuperMarketListPublisher publisher) {
        this.splRepository = splRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.publisher = publisher;
    }

    public List<SuperMarketList> searchAllLists(String userId) throws ApiException {
        try {
            return splRepository.findAllByUserId(userId);
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(AppErrorCode.SUPERMARKET_LIST_QUERY_ERROR);
        }
    }

    public SuperMarketList searchById(String userId, String id) throws ApiException {
        return retrieveSuperMarketList(userId, id);
    }

    public SuperMarketList createList(String userId, SuperMarketListRequest request) throws ApiException {
        validateUser(userId);
        var list = buildSuperMarketList(userId, request);

        var allProductsAvailable = true;
        for (String id: list.getProducts()) {
            try {
                if (productRepository.findById(id).isEmpty()) {
                    allProductsAvailable = false;
                    break;
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
            }
        }

        if (allProductsAvailable) {
            splRepository.save(list);

            var published = publisher.publishCreation(list);
            if (published) {
                log.info("The message about SPL {} was successfully published.", list.getId());
            } else {
                //TODO: Do the rollback of the changes
                // splRepository.delete(list.getId());
            }

            return list;
        } else {
            throw new ApiException(AppErrorCode.PRODUCTS_NOT_FOUND);
        }
    }

    public SuperMarketList updateList(String userId, String id, SuperMarketListRequest request) throws ApiException {
        validateUser(userId);
        var list = retrieveSuperMarketList(userId, id);
        list.setName(request.name());
        list.setProducts(request.products());

        if (!list.getUserId().equals(userId)) {
            throw new ApiException(AppErrorCode.SUPERMARKET_LIST_OPERATION_NOT_ALLOWED);
        }

        var allProductsAvailable = true;
        for (String productId: list.getProducts()) {
            try {
                if (productRepository.findById(productId).isEmpty()) {
                    allProductsAvailable = false;
                    break;
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
            }
        }

        if (allProductsAvailable) {
            splRepository.update(list);

            var published = publisher.publishUpdate(list);
            if (published) {
                log.info("The message about SPL {} was successfully published.", list.getId());
            } else {
                //TODO: Do the rollback of the changes
                // splRepository.delete(list.getId());
            }

            return list;
        } else {
            throw new ApiException(AppErrorCode.PRODUCTS_NOT_FOUND);
        }

    }

    public void removeList(String userId, String id) throws ApiException {
        try {
            var splOpt = splRepository.findByUserIdAndId(userId, id);
            if (splOpt.isPresent()) {
                var spl = splOpt.get();
                splRepository.delete(spl.getId());

                var published = publisher.publishDelete(spl);
                if (published) {
                    log.info("The message about SPL {} was successfully published.", spl.getId());
                } else {
                    //TODO: Do the rollback of the changes
                    // splRepository.delete(list.getId());
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.SUPERMARKET_LIST_QUERY_ERROR);
        }
    }

    private void validateUser(String userId) throws ApiException {
        try {
            userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException(AppErrorCode.USER_NOT_FOUND));
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.USERS_QUERY_ERROR);
        }
    }

    private SuperMarketList buildSuperMarketList(String userId, SuperMarketListRequest request) {
        var id = UUID.randomUUID().toString();
        return new SuperMarketList(id, request.name(), userId, request.products());
    }

    private SuperMarketList retrieveSuperMarketList(String userId, String id) throws ApiException {
        try {
            return splRepository.findByUserIdAndId(userId, id)
                    .orElseThrow(() -> new ApiException(AppErrorCode.SUPERMARKET_LIST_NOT_FOUND));
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.SUPERMARKET_LIST_QUERY_ERROR);
        }
    }
}
