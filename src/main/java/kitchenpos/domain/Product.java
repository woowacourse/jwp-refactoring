package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {

    private static final int ZERO_PRICE = 0;

    private Long id;
    private String name;
    private BigDecimal price;

    protected Product() {
    }

    public Product(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public void validatePriceIsEmpty() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < ZERO_PRICE) {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}
