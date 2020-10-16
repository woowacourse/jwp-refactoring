package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private final String name;
    private final BigDecimal price;
    private final Long id;

    public Product(final Long id, final String name, final BigDecimal price) {
        validateByPrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    private void validateByPrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("해당 상품의 가격은 0 이상이여야 합니다.");
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
