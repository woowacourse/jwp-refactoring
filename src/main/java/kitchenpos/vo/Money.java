package kitchenpos.vo;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    private static final int ZERO = 0;

    private final BigDecimal value;

    private Money(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < ZERO) {
            throw new IllegalArgumentException("금액은 0 이하 혹은 null일 수 없습니다.");
        }
    }

    public static Money valueOf(final long value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money valueOf(final BigDecimal value) {
        return new Money(value);
    }

    public boolean isBiggerThan(final Money other) {
        return this.value.compareTo(other.value) > 0;
    }

    public Money add(final Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money multiply(final Money other) {
        return new Money(this.value.multiply(other.getValue()));
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        final Money money = (Money) o;
        return Objects.equals(value, money.value);
    }
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
