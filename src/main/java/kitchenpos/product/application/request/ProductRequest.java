package kitchenpos.product.application.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    @JsonCreator
    public ProductRequest(final String name, final BigDecimal price) {
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
