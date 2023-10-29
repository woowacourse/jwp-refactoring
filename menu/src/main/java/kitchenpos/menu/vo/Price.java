package kitchenpos.menu.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    public static final Price ZERO = new Price(BigDecimal.ZERO);

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    private Price(BigDecimal value) {
        this.value = value;
    }

    protected Price() {
    }

    public static Price valueOf(BigDecimal value) {
        return new Price(value);
    }

    public Price add(Price price) {
        return new Price(this.value.add(price.value));
    }

    public boolean isGreaterThan(Price money) {
        return this.value.compareTo(money.value) > 0;
    }

    public Price multiply(long number) {
        return new Price(value.multiply(BigDecimal.valueOf(number)));
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
