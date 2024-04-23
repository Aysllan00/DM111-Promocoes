package br.inatel.dm111.api.promocao.controller;

import br.inatel.dm111.api.core.ApiException;
import br.inatel.dm111.api.promocao.PromocaoRequest;
import br.inatel.dm111.api.promocao.service.PromocaoService;
import br.inatel.dm111.persistence.promocao.Promocao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// http://localhost:8080/dm111/users/{userId}/promocoes
@RestController
@RequestMapping("/dm111")
public class PromocaoController {

    private final PromocaoService service;

    public PromocaoController(PromocaoService service) {
        this.service = service;
    }

    @GetMapping("/promocao")
    public ResponseEntity<List<Promocao>> getAllPromocoes(@PathVariable("userId") String userId) throws ApiException {
        var promocoes = service.searchAllPromocoes(userId);
        return ResponseEntity.ok(promocoes);
    }

    @GetMapping("/promocao/{id}")
    public ResponseEntity<Promocao> getPromocao(@PathVariable("userId") String userId,
                                                @PathVariable("id") String id)
            throws ApiException {
        var promocao = service.searchById(userId, id);
        return ResponseEntity.ok(promocao);
    }

    @GetMapping("/promocoes")
    public ResponseEntity<List<Promocao>> getPromocoes() throws ApiException {
        var promocoes = service.searchPromocoes();
        return ResponseEntity.ok(promocoes);
    }

    @GetMapping("/promocoes/{id}")
    public ResponseEntity<Promocao> getPromocao(@PathVariable("id") String id) throws ApiException {
        var promocao = service.searchPromocao(id);
        return ResponseEntity.ok(promocao);
    }

    @GetMapping("/promocoes/users/{userId}")
    public ResponseEntity<List<Promocao>> getPromocoesForUser(@PathVariable("userId") String userId) throws ApiException {
        var promocoes = service.searchPromocoesForUser(userId);
        return ResponseEntity.ok(promocoes);
    }

    @PostMapping("/promocao")
    public ResponseEntity<Promocao> postPromocao(@RequestBody PromocaoRequest request) {
        var promocao = service.createPromocao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(promocao);
    }

    @PutMapping("/promocao/{id}")
    public ResponseEntity<Promocao> putPromocao(@PathVariable("id") String id,
                                                @RequestBody PromocaoRequest request) throws ApiException {
        var promocao = service.updatePromocao(id, request);
        return ResponseEntity.ok(promocao);
    }

    @DeleteMapping("/promocao/{id}")
    public ResponseEntity<?> deletePromocao(@PathVariable("id")String id) throws ApiException {
        service.removePromocao(id);
        return ResponseEntity.noContent().build();
    }
}