package kitchenpos.generic;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

public class Money {
    public static final Money ZERO = new Money(BigDecimal.ZERO);
    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("올바르지 않은 Money입니다.");
        }
        this.amount = amount;
    }

    public static Money of(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money of(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static <T> Money sum(Collection<T> bags, Function<T, Money> monetary) {
        return bags.stream().map(monetary).reduce(ZERO, Money::plus);
    }

    public Money plus(Money target) {
        return new Money(this.amount.add(target.amount));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Money times(long value) {
        return new Money(amount.multiply(BigDecimal.valueOf(value)));
    }

    public boolean isLessThanOrEqual(Money other) {
        return this.amount.compareTo(other.amount) <= 0;
    }
}
