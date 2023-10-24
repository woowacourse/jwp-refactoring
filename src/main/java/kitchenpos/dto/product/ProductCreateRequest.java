package kitchenpos.dto.product;

public class ProductCreateRequest {

    private String name;
    private Long price;

    private ProductCreateRequest() {
    }

    public ProductCreateRequest(
            final String name,
            final Long price
    ) {
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
