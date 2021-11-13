package kitchenpos.product.dto;

public class ProductRequest {

    private String name;
    private double price;

    public ProductRequest() {

    }

    public ProductRequest(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
