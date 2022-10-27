package kitchenpos.dto.product;

public class ProductCreateRequest {

    private String name;
    private int price;

    public ProductCreateRequest() {
    }

    public ProductCreateRequest(String name, int price) {
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
