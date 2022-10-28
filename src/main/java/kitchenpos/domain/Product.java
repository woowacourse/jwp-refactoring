package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    public Product(final Long id, final String name, final BigDecimal price) {
        validate(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validate(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("상품의 가격이 음수가 될 수 없습니다.");
        }
    }

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
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
