package kitchenpos.product.service.dto;

public class ProductCreateRequest {

    private final String name;
    private final Long price;

    public ProductCreateRequest(final String name, final Long price) {
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
