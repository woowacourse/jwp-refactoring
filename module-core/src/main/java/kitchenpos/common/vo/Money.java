package kitchenpos.common.vo;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Money {

    private static final Money ZERO = Money.valueOf(0);

    private BigDecimal value;

    protected Money() {
    }

    private Money(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("금액은 0 이하 혹은 null일 수 없습니다.");
        }
    }

    public static Money valueOf(final long value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money valueOf(final BigDecimal value) {
        return new Money(value);
    }

    public static Money empty() {
        return ZERO;
    }

    public boolean isSmallerThan(final Money other) {
        return this.value.compareTo(other.value) < 0;
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
