package kitchenpos.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;

public class ProductCreationRequest {

    private final String name;
    private final BigDecimal price;

    @JsonCreator
    public ProductCreationRequest(final String name, final BigDecimal price) {
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
