package br.inatel.dm111.persistence.promocao;

import br.inatel.dm111.api.promocao.service.ProductForYou;
import br.inatel.dm111.persistence.promocaoproduto.PromocaoProduto;

import java.util.Date;
import java.util.List;

public class Promocao {

    private String id;
    private String name;
    private String starting;
    private String expiration;
    private List<PromocaoProduto> products;
    private List<ProductForYou> productsForYou; // Adicionando a lista de produtos relevantes para o usuário


    public Promocao() {
    }

    public Promocao(String id, String name, String starting, String expiration, List<PromocaoProduto> products) {
        this.id = id;
        this.name = name;
        this.starting = starting;
        this.expiration = expiration;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStarting() {
        return starting;
    }

    public void setStarting(String starting) {
        this.starting = starting;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public List<PromocaoProduto> getProducts() {
        return products;
    }

    public void setProducts(List<PromocaoProduto> products) {
        this.products = products;
    }

    public List<ProductForYou> getProductsForYou() {
        return productsForYou;
    }

    public void setProductsForYou(List<ProductForYou> productsForYou) {
        this.productsForYou = productsForYou;
    }
}