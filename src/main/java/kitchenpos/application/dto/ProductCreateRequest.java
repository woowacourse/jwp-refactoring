package kitchenpos.application.dto;

public class ProductCreateRequest {
    private String name;
    private Long price;

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
