package kitchenpos.domain;

import java.math.BigDecimal;

public class Product {

    private Long id;
    private String name;
    private BigDecimal price;

    public Product() {
    }

    public Product(final String name, final BigDecimal price) {
        validateNotNull(price);
        validateNotNegative(price);
        this.name = name;
        this.price = price;
    }

    private void validateNotNull(final BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNotNegative(final BigDecimal decimal) {
        if (decimal.compareTo(BigDecimal.ZERO) < 0) {
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
