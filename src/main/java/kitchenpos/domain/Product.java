package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    public Product(final Long id, final String name, final BigDecimal price) {
        validatePrice(price);

        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    private void validatePrice(final BigDecimal price) {
        new Price(price);
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
