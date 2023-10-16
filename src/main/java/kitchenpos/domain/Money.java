package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.KitchenPosException;

@Embeddable
public class Money {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    @Column(precision = 19, scale = 2, name = "money")
    private BigDecimal value;

    public static Money from(long value) {
        if (value == 0) {
            return ZERO;
        }
        return new Money(BigDecimal.valueOf(value));
    }

    protected Money() {
    }

    public Money(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(BigDecimal value) {
        if (value == null) {
            throw new KitchenPosException("금액은 null이 될 수 없습니다.");
        }
    }

    public Money plus(Money money) {
        if (this == ZERO) {
            return money;
        }
        if (money == ZERO) {
            return this;
        }
        return new Money(this.value.add(money.value));
    }

    public boolean isNegative() {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean lessThen(Money other) {
        return value.compareTo(other.value) < 0;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Money)) {
            return false;
        }

        Money money = (Money) o;

        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
