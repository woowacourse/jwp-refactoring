package kitchenpos.application.dto;

public class ProductCreateRequest {

    private String name;
    private Integer price;

    public ProductCreateRequest(final String name, final Integer price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
