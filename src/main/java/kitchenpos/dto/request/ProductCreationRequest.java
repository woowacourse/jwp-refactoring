package kitchenpos.dto.request;

import java.math.BigDecimal;

public class ProductCreationRequest {

    private final String name;
    private final BigDecimal price;

    public ProductCreationRequest(String name, BigDecimal price) {
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
