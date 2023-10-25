package kitchenpos.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Money {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    private Money(BigDecimal value) {
        this.value = value;
    }

    protected Money() {
    }

    public static Money valueOf(BigDecimal value) {
        return new Money(value);
    }

    public Money add(Money money) {
        return new Money(this.value.add(money.value));
    }

    public boolean isGreaterThan(Money money) {
        return this.value.compareTo(money.value) > 0;
    }

    public Money multiply(long number) {
        return new Money(value.multiply(BigDecimal.valueOf(number)));
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
        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Money{" +
                "value=" + value +
                '}';
    }
}
