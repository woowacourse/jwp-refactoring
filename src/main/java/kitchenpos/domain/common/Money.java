package kitchenpos.domain.common;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Money implements Comparable<Money> {
    public static final BigDecimal MIN = BigDecimal.ZERO;
    public static final BigDecimal MAX = BigDecimal.valueOf(92233720368547758.08);
    public static final Money ZERO = new Money(BigDecimal.ZERO);
    private BigDecimal amount;

    protected Money() {
    }

    private Money(final BigDecimal amount) {
        validateMoney(amount);
        this.amount = amount;
    }

    private static void validateMoney(final BigDecimal amount) {
        if (amount.compareTo(MIN) < 0) {
            throw new IllegalArgumentException("금액은 0보다 작을 수 없습니다.");
        }
        if (amount.scale() > 2) {
            throw new IllegalArgumentException("금액은 소수점 2자리까지만 입력 가능합니다.");
        }
        if (amount.compareTo(MAX) >= 0) {
            throw new IllegalArgumentException("최대 금액을 초과하였습니다.");
        }
    }

    public static Money valueOf(final long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money valueOf(final BigDecimal amount) {
        return new Money(amount);
    }

    public Money add(final Money money) {
        return new Money(amount.add(money.amount));
    }

    public Money multiply(final long value) {
        return new Money(amount.multiply(BigDecimal.valueOf(value)));
    }

    public boolean isBiggerThan(final Money money) {
        return amount.compareTo(money.amount) >= 0;
    }

    @Override
    public int compareTo(final Money o) {
        return amount.compareTo(o.amount);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
