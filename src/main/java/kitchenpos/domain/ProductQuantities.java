package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

public class ProductQuantities {

    private final Map<Product, Quantity> values;

    public ProductQuantities(final Map<Product, Quantity> values) {
        this.values = values;
    }

    public BigDecimal sum() {
        return values.entrySet().stream()
                .map(this::toMultipliedValue)
                .reduce(BigDecimal::add)
                .orElseThrow(IllegalArgumentException::new);
    }

    private BigDecimal toMultipliedValue(final Entry<Product, Quantity> entry) {
        BigDecimal quantity = BigDecimal.valueOf(entry.getValue().getValue());
        return entry.getKey().multiply(quantity);
    }
}
