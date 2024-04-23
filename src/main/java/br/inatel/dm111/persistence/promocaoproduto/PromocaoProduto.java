package br.inatel.dm111.persistence.promocaoproduto;

public class PromocaoProduto {

    private String productId;
    private int discount;

    public PromocaoProduto() {
    }

    public PromocaoProduto(String productId, int discount) {
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