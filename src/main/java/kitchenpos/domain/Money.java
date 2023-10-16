package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Money {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    private BigDecimal value;

    protected Money() {
    }

    public Money(BigDecimal value) {
        this.value = value;
    }

    public Money add(Money other) {
        return new Money(value.add(other.value));
    }

    public boolean isGreaterThan(Money other) {
        return value.compareTo(other.value) > 0;
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
        Money money = (Money) o;
        return Objects.equals(value.doubleValue(), money.value.doubleValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.doubleValue());
    }
}
