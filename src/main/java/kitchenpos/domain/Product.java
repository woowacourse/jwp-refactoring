package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private BigDecimal price;

    public Product() {
    }

    public Product(final Long id, final String name, final BigDecimal price) {
        validateProductPrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validateProductPrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("[ERROR] 부적절한 상품 가격입니다.");
        }
    }

    public static Product ofNullId(final String name, final BigDecimal price) {
        return new Product(null, name, price);
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
