package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.Embeddable;

import static javax.persistence.AccessType.FIELD;

@Embeddable
@Access(FIELD)
public class Money {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    private BigDecimal value;

    public static Money of(int value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money of(long value) {
        return new Money(BigDecimal.valueOf(value));
    }

    protected Money() {
    }

    public Money(final BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격이 없거나 0보다 작습니다.");
        }
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Money times(final int quantity) {
        return new Money(value.multiply(BigDecimal.valueOf(quantity)));
    }

    public Money plus(final Money other) {
        return new Money(value.add(other.value));
    }

    public boolean isHigherThan(final Money other) {
        return value.compareTo(other.value) > 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Money money = (Money) o;
        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
