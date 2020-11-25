package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private static final BigDecimal MINIMUM_PRICE = BigDecimal.ZERO;

    private Long id;
    private String name;
    private BigDecimal price;

    public Product() {
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
        validate();
    }

    private void validate() {
        if (Objects.isNull(price) || price.compareTo(MINIMUM_PRICE) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }
}
