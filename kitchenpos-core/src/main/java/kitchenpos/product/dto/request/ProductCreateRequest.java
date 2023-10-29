package kitchenpos.product.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;

public class ProductCreateRequest {

    private final String name;
    private final BigDecimal price;

    @JsonCreator
    public ProductCreateRequest(String name, BigDecimal price) {
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
