package kitchenpos.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;

public class ProductRequest {

    private final String name;

    private final BigDecimal price;

    @JsonCreator
    public ProductRequest(String name, BigDecimal price) {
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
