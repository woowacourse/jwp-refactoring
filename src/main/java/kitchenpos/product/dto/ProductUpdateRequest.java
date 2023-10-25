package kitchenpos.product.dto;

public class ProductUpdateRequest {

    private String name;
    private Long price;

    private ProductUpdateRequest() {
    }

    public ProductUpdateRequest(String name, Long price) {
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
