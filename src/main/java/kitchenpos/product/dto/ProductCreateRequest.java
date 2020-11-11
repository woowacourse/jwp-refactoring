package kitchenpos.product.dto;

public class ProductCreateRequest {
    private String name;
    private Long price;

    private ProductCreateRequest() {
    }

    public ProductCreateRequest(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }
}
