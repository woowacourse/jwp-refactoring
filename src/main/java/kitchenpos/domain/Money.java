package kitchenpos.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Money {
    public static final Money ZERO = new Money(0L);

    @Column(name = "price")
    private Long value;

    public Money() {
    }

    public Money(Long value) {
        this.value = value;
    }

    public Money plus(Money money) {
        return new Money(money.value + this.value);
    }

    public Long compareTo(Money targetMoney) {
        return this.value - targetMoney.value;
    }

    public Money multiply(long quantity) {
        return new Money(this.value * quantity);
    }

    public boolean isMinus() {
        return value < 0;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Money money = (Money)o;
        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
