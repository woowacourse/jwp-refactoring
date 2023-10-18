package kitchenpos.dto;

public class ProductRequest {

    private String name;

    private Integer price;

    public ProductRequest(final String name, final Integer price) {
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
