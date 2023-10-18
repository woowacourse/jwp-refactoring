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
    private BigDecimal amount;

    public static Money from(long value) {
        if (value == 0) {
            return ZERO;
        }
        return new Money(BigDecimal.valueOf(value));
    }

    protected Money() {
    }

    public Money(BigDecimal amount) {
        validate(amount);
        this.amount = amount;
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
        return new Money(this.amount.add(money.amount));
    }

    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isLessThan(Money other) {
        return amount.compareTo(other.amount) < 0;
    }

    public boolean isGreaterThan(Money other) {
        return amount.compareTo(other.amount) > 0;
    }

    public Money multiple(long multiplicand) {
        if (multiplicand == 0) {
            return ZERO;
        }
        return new Money(amount.multiply(BigDecimal.valueOf(multiplicand)));
    }

    public BigDecimal getAmount() {
        return amount;
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

        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return amount != null ? amount.hashCode() : 0;
    }
}
