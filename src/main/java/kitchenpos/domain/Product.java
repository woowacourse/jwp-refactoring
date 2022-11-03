package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private BigDecimal price;

    public Product() {
    }

    public Product(final String name, final BigDecimal price) {
        validateProductPrice(price);
        this.name = name;
        this.price = price;
    }

    public Product(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validateProductPrice(final BigDecimal price) {
        if (isPriceNullOrMinus(price)) {
            throw new IllegalArgumentException("상품의 가격은 비어있거나 0보다 작을 수 없습니다.");
        }
    }

    private boolean isPriceNullOrMinus(final BigDecimal price) {
        return Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0;
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
