package kitchenpos.dto;

public class ProductCreateRequest {

    private final String name;
    private final Long price;

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
