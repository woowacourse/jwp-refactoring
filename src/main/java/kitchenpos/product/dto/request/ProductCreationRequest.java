package kitchenpos.product.dto.request;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class ProductCreationRequest {

    @NotNull
    private final String name;
    @NotNull
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
