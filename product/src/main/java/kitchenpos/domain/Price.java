package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

import static java.util.Objects.isNull;

@Embeddable
public class Price {

    public static final Price ZERO = new Price(BigDecimal.ZERO);

    @Column(name = "price")
    private BigDecimal value;

    protected Price() {
    }

    public Price(final BigDecimal value) {
        if (isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("금액이 없거나 음수입니다.");
        }

        this.value = value;
    }

    public static Price valueOf(final int price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public Price multiply(final long value) {
        final BigDecimal result = this.value.multiply(BigDecimal.valueOf(value));
        return new Price(result);
    }

    public Price add(final Price other) {
        final BigDecimal result = value.add(other.value);
        return new Price(result);
    }

    public boolean isBiggerThan(final Price other) {
        final int result = value.compareTo(other.value);
        return result > 0;
    }

    public boolean equalsWith(final long value) {
        final int result = this.value.compareTo(BigDecimal.valueOf(value));
        return result == 0;
    }

    public BigDecimal getValue() {
        return value;
    }
}
