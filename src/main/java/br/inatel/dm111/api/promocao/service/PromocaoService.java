package br.inatel.dm111.api.promocao.service;

import br.inatel.dm111.api.core.ApiException;
import br.inatel.dm111.api.core.AppErrorCode;
import br.inatel.dm111.api.promocao.PromocaoRequest;
import br.inatel.dm111.api.supermaketlist.service.SuperMarketListService;
import br.inatel.dm111.persistence.product.Product;
import br.inatel.dm111.persistence.promocao.Promocao;
import br.inatel.dm111.persistence.promocao.PromocaoRepository;
import br.inatel.dm111.persistence.promocaoproduto.PromocaoProduto;
import br.inatel.dm111.persistence.supermarketlist.SuperMarketList;
import br.inatel.dm111.persistence.supermarketlist.SuperMarketListRepository;
import br.inatel.dm111.persistence.user.UserFirebaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
public class PromocaoService {

    private static final Logger log = LoggerFactory.getLogger(PromocaoService.class);

    private final PromocaoRepository promocaoRepository;
    private final UserFirebaseRepository userRepository;
    private final SuperMarketListService superMarketListService;

    private final SuperMarketListRepository splRepository;

    public PromocaoService(PromocaoRepository promocaoRepository, UserFirebaseRepository userRepository,
                           SuperMarketListService superMarketListService, SuperMarketListRepository splRepository) {
        this.promocaoRepository = promocaoRepository;
        this.userRepository = userRepository;
        this.superMarketListService = superMarketListService;
        this.splRepository = splRepository;
    }

    public List<Promocao> searchAllPromocoes(String userId) throws ApiException {
        try {
            return promocaoRepository.findAllByUserId(userId);
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(AppErrorCode.PROMOCAO_QUERY_ERROR);
        }
    }

    public List<Promocao> searchPromocoes() throws ApiException {
        try {
            LocalDate currentDate = LocalDate.now();
            return promocaoRepository.findAll().stream()
                    .filter(promocao -> isValidPromocao(promocao, currentDate))
                    .collect(Collectors.toList());
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
        }
    }

    private boolean isValidPromocao(Promocao promocao, LocalDate currentDate) {
        LocalDate startingDate = parseDate(promocao.getStarting());
        LocalDate expirationDate = parseDate(promocao.getExpiration());
        return currentDate.compareTo(startingDate) >= 0 && currentDate.compareTo(expirationDate) <= 0;
    }

    private LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateStr, formatter);
    }

    public Promocao searchById(String userId, String id) throws ApiException {
        return retrievePromocao(userId, id);
    }

    public Promocao searchPromocao(String id) throws ApiException {
        var promocao = retrievePromocao(id);
        if (promocao == null) {
            return null; // Retorna null se a promoção não for encontrada
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate startingDate = parseDate(promocao.getStarting());
        LocalDate expirationDate = parseDate(promocao.getExpiration());
        if (currentDate.compareTo(startingDate) >= 0 && currentDate.compareTo(expirationDate) <= 0) {
            return promocao; // Retorna a promoção se for válida
        } else {
            return null; // Retorna null se a promoção não for válida
        }
    }

    //---------------------------------------------------------------------------

    public List<Promocao> searchPromocoesForUser(String userId) throws ApiException {
        try {
            // Buscar todas as promoções válidas
            List<Promocao> allPromocoes = promocaoRepository.findAll();

            // Filtrar as promoções válidas para o dia atual
            LocalDate currentDate = LocalDate.now();
            List<Promocao> validPromocoes = allPromocoes.stream()
                    .filter(promocao -> isValidPromocaoUser(promocao, currentDate))
                    .collect(Collectors.toList());

            // Encontrar produtos relevantes para o usuário em cada promoção válida
            return validPromocoes.stream()
                    .map(promocao -> addProductsForUser(promocao, userId))
                    .collect(Collectors.toList());
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error while searching promotions for user: {}", e.getMessage());
            // Se ocorrer um erro, retornar uma lista vazia
            return Collections.emptyList();
        }
    }

    private boolean isValidPromocaoUser(Promocao promocao, LocalDate currentDate) {
        LocalDate startingDate = parseDateUser(promocao.getStarting());
        LocalDate expirationDate = parseDateUser(promocao.getExpiration());
        return currentDate.compareTo(startingDate) >= 0 && currentDate.compareTo(expirationDate) <= 0;
    }

    private LocalDate parseDateUser(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateStr, formatter);
    }

    private Promocao addProductsForUser(Promocao promocao, String userId) {
        List<ProductForYou> matchingProducts = findMatchingProductsForUser(promocao, userId);
        promocao.setProductsForYou(matchingProducts);
        return promocao;
    }

    private List<ProductForYou> findMatchingProductsForUser(Promocao promocao, String userId) {
        try {
            // Obter as últimas listas de supermercado do usuário
            List<SuperMarketList> userLists = splRepository.findAllByUserId(userId);

            // Verifica se há listas de supermercado para o usuário
            if (userLists.isEmpty()) {
                log.warn("Nenhuma lista de supermercado encontrada para o usuário {}", userId);
                return Collections.emptyList(); // Retorna uma lista vazia
            }

            // Extrair os produtos das listas de supermercado
            Set<String> userProducts = userLists.stream()
                    .flatMap(list -> list.getProducts().stream())
                    .collect(Collectors.toSet());

            log.info("Produtos da lista de supermercado do usuário: {}", userProducts);

            // Filtrar os produtos da promoção que estão na lista de produtos do usuário
            List<ProductForYou> matchingProducts = promocao.getProducts().stream()
                    .peek(promocaoProduto -> log.info("ID do produto da promoção: {}", promocaoProduto.getProductId()))
                    .map(promocaoProduto -> new ProductForYou(promocaoProduto.getProductId(), promocaoProduto.getDiscount()))
                    .filter(productForYou -> {
                        boolean contains = userProducts.contains(productForYou.getProductId());
                        log.info("O produto {} está presente na lista de produtos do usuário? {}", productForYou.getProductId(), contains);
                        return contains;
                    })
                    .collect(Collectors.toList());

            log.info("Produtos correspondentes para o usuário: {}", matchingProducts);
            return matchingProducts;

        } catch (ExecutionException | InterruptedException e) {
            log.error("Error while finding matching products for user: {}", e.getMessage());
            // Se ocorrer um erro, retornar uma lista vazia
            return Collections.emptyList();
        }
    }



    // --------------------------------------------------------------------------


    public Promocao createPromocao(PromocaoRequest request) {
        var promocao = buildPromocao(request);
        promocaoRepository.save(promocao);
        return promocao;
    }

    public Promocao updatePromocao(String id, PromocaoRequest request) throws ApiException {
        var promocao = retrievePromocao(id);
        promocao.setName(request.name());
        promocao.setStarting(request.starting());
        promocao.setExpiration(request.expiration());
        promocao.setProducts(request.products());

        promocaoRepository.update(promocao);
        return promocao;
    }

    public void removePromocao(String id) throws ApiException {
        try {
            promocaoRepository.delete(id);
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PROMOCAO_QUERY_ERROR);
        }
    }

    private Promocao buildPromocao(PromocaoRequest request) {
        var id = UUID.randomUUID().toString();
        return new Promocao(id,
                request.name(),
                request.starting(),
                request.expiration(),
                request.products());
    }

    private Promocao retrievePromocao(String userId, String id) throws ApiException {
        try {
            return promocaoRepository.findByUserIdAndId(userId, id)
                    .orElseThrow(() -> new ApiException(AppErrorCode.PROMOCAO_NOT_FOUND));
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PROMOCAO_QUERY_ERROR);
        }
    }

    private Promocao retrievePromocao(String id) throws ApiException {
        try {
            var promocao = promocaoRepository.findByUserId(id);
            if (promocao != null) {
                return promocao;
            } else {
                throw new ApiException(AppErrorCode.PROMOCAO_NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PROMOCAO_QUERY_ERROR);
        }
    }

}