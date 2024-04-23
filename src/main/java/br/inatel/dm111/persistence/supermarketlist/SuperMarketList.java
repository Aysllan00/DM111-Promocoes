package br.inatel.dm111.persistence.supermarketlist;

import java.util.List;

//{
//    "id": "",
//    "name": "Materiais e limpeza",
//    "userId": "",
//    "products": []
//}
public class SuperMarketList {

    private String id;
    private String name;
    private String userId;
    private List<String> products;

    public SuperMarketList() {
    }

    public SuperMarketList(String id, String name, String userId, List<String> products) {
        this.id = id;
        this.name = name;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }
}
