package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    public static Price ZERO = new Price(BigDecimal.ZERO);

    @Column
    private BigDecimal price;

    protected Price() {
    }

    public Price(final BigDecimal price) {
        validatePositive(price);
        this.price = price;
    }

    private void validatePositive(final BigDecimal price) {
        if (BigDecimal.ZERO.compareTo(price) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public Price add(final Price other) {
        BigDecimal added = other.price.add(price);
        return new Price(added);
    }

    public Price multiply(final long number) {
        BigDecimal multiplied = price.multiply(BigDecimal.valueOf(number));
        return new Price(multiplied);
    }

    public boolean isGreaterThan(final Price other) {
        return price.compareTo(other.price) > 0;
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
        final Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
