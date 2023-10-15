package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.util.BigDecimalUtil;

public class Product {

    private Long id;
    private String name;
    private BigDecimal price;

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    public Product(final Long id, final String name, final BigDecimal price) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validatePrice(final BigDecimal price) {
        BigDecimalUtil.valueForCompare(price)
                .throwIfNegative(IllegalArgumentException::new);
    }

    public BigDecimal getTotalPrice(final BigDecimal quantity) {
        return price.multiply(quantity);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
