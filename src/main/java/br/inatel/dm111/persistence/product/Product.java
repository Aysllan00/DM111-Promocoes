package br.inatel.dm111.persistence.product;


//{
//    "id": "",
//    "name": "Detergente, maça",
//    "brand": "Tia da esquina",
//    "unit": "Kg||ml",
//    "amount": "500",
//    "price": 2.5
//}
public class Product {
    private String id;
    private String name;
    private String brand;
    private String unit;
    private long amount;
    private long price;

    public Product() {
    }

    public Product(String id, String name, String brand, String unit, long amount, long price) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.unit = unit;
        this.amount = amount;
        this.price = price;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
