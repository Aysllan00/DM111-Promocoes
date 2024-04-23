package br.inatel.dm111.api.supermaketlist.controller;

import br.inatel.dm111.api.core.ApiException;
import br.inatel.dm111.api.product.ProductRequest;
import br.inatel.dm111.api.supermaketlist.SuperMarketListRequest;
import br.inatel.dm111.api.supermaketlist.service.SuperMarketListService;
import br.inatel.dm111.persistence.product.Product;
import br.inatel.dm111.persistence.supermarketlist.SuperMarketList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//http://localhost:8080/dm111/users/{userId}/supermarketlist
@RestController
@RequestMapping("/dm111")
public class SuperMarketListController {

    private final SuperMarketListService service;

    public SuperMarketListController(SuperMarketListService service) {
        this.service = service;
    }

    @GetMapping("/users/{userId}/supermarketlist")
    public ResponseEntity<List<SuperMarketList>> getAllSuperMarketList(@PathVariable("userId") String userId) throws ApiException {
        var list = service.searchAllLists(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/users/{userId}/supermarketlist/{id}")
    public ResponseEntity<SuperMarketList> getSuperMarketList(@PathVariable("userId") String userId,
                                                              @PathVariable("id") String id)
            throws ApiException {
        var list = service.searchById(userId, id);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/users/{userId}/supermarketlist")
    public ResponseEntity<SuperMarketList> postSuperMarketList(@PathVariable("userId") String userId,
            @RequestBody SuperMarketListRequest request)
            throws ApiException {
        var list = service.createList(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(list);
    }

    @PutMapping("/users/{userId}/supermarketlist/{id}")
    public ResponseEntity<SuperMarketList> putSuperMarketList(@PathVariable("userId") String userId,
                                                              @PathVariable("id") String id,
                                                      @RequestBody SuperMarketListRequest request)
            throws ApiException {
        var list = service.updateList(userId, id, request);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/users/{userId}/supermarketlist/{id}")
    public ResponseEntity<?> deleteSuperMarketList(@PathVariable("userId") String userId,
                                                   @PathVariable("id") String id) throws ApiException {
        service.removeList(userId, id);
        return ResponseEntity.noContent().build();
    }
}
