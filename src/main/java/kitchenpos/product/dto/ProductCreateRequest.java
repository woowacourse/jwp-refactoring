package kitchenpos.product.dto;

public class ProductCreateRequest {

    private String name;
    private long price;

    private ProductCreateRequest() {
    }

    public ProductCreateRequest(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
