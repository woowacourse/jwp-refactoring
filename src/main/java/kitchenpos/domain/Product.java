package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.util.BigDecimalUtil;

public class Product {

    private Long id;
    private String name;
    private BigDecimal price;

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    public Product(final Long id, final String name, final BigDecimal price) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validatePrice(final BigDecimal price) {
        BigDecimalUtil.valueForCompare(price)
                .throwIfNegative(IllegalArgumentException::new);
    }

    public BigDecimal getTotalPrice(final BigDecimal quantity) {
        return price.multiply(quantity);
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Product product = (Product) o;
        if (Objects.isNull(this.id) || Objects.isNull(product.id)) {
            return false;
        }
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
