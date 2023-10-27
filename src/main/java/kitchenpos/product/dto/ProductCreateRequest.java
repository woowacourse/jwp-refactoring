package kitchenpos.product.dto;

import javax.validation.constraints.NotNull;

public class ProductCreateRequest {

    @NotNull
    private final String name;

    @NotNull
    private final Long price;

    public ProductCreateRequest(final String name, final long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
