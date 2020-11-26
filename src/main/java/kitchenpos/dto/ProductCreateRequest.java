package kitchenpos.dto;

import javax.validation.constraints.NotNull;

public class ProductCreateRequest {

    @NotNull
    private String name;

    @NotNull
    private Long price;

    public ProductCreateRequest() {
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
