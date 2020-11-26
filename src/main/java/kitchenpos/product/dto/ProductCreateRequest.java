package kitchenpos.product.dto;

import com.sun.istack.NotNull;

public class ProductCreateRequest {
    @NotNull
    private String name;
    @NotNull
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
