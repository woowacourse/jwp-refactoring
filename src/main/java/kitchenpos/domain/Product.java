package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.exception.IllegalPriceException;

public class Product {
    private Long id;
    private String name;
    private BigDecimal price;

    public Product(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public BigDecimal calculateTotalPrice(final long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public void validatePrice() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalPriceException();
        }
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
