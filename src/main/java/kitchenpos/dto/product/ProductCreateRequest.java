package kitchenpos.dto.product;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductCreateRequest {

    private String name;
    private BigDecimal price;

    public ProductCreateRequest(final String name, final BigDecimal price) {
        validatePrice(price);
        this.name = name;
        this.price = price;
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException();
        }
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
