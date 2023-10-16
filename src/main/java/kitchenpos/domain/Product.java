package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    private Product(final Long id,
                    final String name,
                    final BigDecimal price) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product of(final String name,
                             final Long price) {
        if (price == null) {
            throw new IllegalArgumentException("상품의 가격은 null일 수 없습니다.");
        }
        return new Product(null, name, BigDecimal.valueOf(price));
    }

    public static Product of(final Long id,
                             final String name,
                             final BigDecimal price) {
        return new Product(id, name, price);
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
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
