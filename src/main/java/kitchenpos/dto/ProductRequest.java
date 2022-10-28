package kitchenpos.dto;

public class ProductRequest {

    private String name;
    private int price;

    public ProductRequest() {
    }

    public ProductRequest(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
