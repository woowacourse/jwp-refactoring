package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Map;

public class ProductQuantity {

    private final Map<Product, Quantity> values;

    public ProductQuantity(final Map<Product, Quantity> values) {
        this.values = values;
    }

    public BigDecimal sum() {
        return values.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue().getValue())))
                .reduce(BigDecimal::add)
                .orElseThrow(IllegalArgumentException::new);
    }
}
