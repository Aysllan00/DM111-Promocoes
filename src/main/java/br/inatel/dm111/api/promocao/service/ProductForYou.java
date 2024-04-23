package br.inatel.dm111.api.promocao.service;

public class ProductForYou {

    private String productId;
    private int discount;

    // Construtor, getters e setters

    public ProductForYou(String productId, int discount) {
        this.productId = productId;
        this.discount = discount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
