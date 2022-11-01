package kitchenpos.product.dto.request;

import java.math.BigDecimal;

public class CreateProductRequest {

    private String name;
    private BigDecimal price;

    private CreateProductRequest() {
    }

    public CreateProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
