package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price implements Comparable<Price> {

    @Column(name = "price")
    private final BigDecimal value;

    protected Price() {
        this.value = BigDecimal.valueOf(0);
    }

    public Price(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("가격은 null일 수 없습니다.");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 음수일 수 없습니다.");
        }
    }

    public Price plus(Price otherPrice) {
        return new Price(value.add(otherPrice.value));
    }

    public Price multiply(long quantity) {
        return new Price(value.multiply(BigDecimal.valueOf(quantity)));
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
        Price price1 = (Price) o;
        return value.equals(price1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(Price other) {
        return value.compareTo(other.value);
    }
}
