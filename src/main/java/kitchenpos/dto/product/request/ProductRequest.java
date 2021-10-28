package kitchenpos.dto.product.request;

public class ProductRequest {

    private final String name;
    private final Integer price;

    public ProductRequest(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public ProductRequest() {
        this(null, null);
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
