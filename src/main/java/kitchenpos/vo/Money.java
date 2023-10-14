package kitchenpos.vo;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

public class Money {

    public static final Money ZERO = Money.valueOf(0);

    private final BigDecimal amount;

    Money(BigDecimal amount) {
        this.amount = amount;
    }

    public static Money valueOf(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money valueOf(BigDecimal amount) {
        return new Money(amount);
    }

    public static <T> Money sum(Collection<T> collection, Function<T, Money> monetary) {
        return collection.stream()
                .map(monetary)
                .reduce(Money.ZERO, Money::plus);
    }

    public Money plus(Money amount) {
        return new Money(this.amount.add(amount.amount));
    }

    public Money times(double multiplicand) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(multiplicand)));
    }

    public boolean isLessThan(Money other) {
        return amount.compareTo(other.amount) < 0;
    }

    public boolean isGreaterThan(Money other) {
        return amount.compareTo(other.amount) > 0;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Money)) {
            return false;
        }

        Money object = (Money) o;
        return Objects.equals(amount.doubleValue(), object.amount.doubleValue());
    }

    public int hashCode() {
        return Objects.hashCode(amount);
    }
}
