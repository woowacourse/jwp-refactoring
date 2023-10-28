package kitchenpos.domain.common;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Money implements Comparable<Money> {
    public static final BigDecimal MIN = BigDecimal.ZERO;
    public static final BigDecimal MAX = BigDecimal.valueOf(92233720368547758.08);
    public static final Money ZERO = new Money(BigDecimal.ZERO);
    public static final String AMOUNT_CANNOT_BE_BELOW_ZERO_ERROR_MESSAGE = "금액은 0보다 작을 수 없습니다.";
    public static final String AMOUNT_IS_NOT_IN_SCALE_TWO_ERROR_MESSAGE = "금액은 소수점 2자리까지만 입력 가능합니다.";
    public static final String AMOUNT_EXCEEDS_OEVR_MAX_AMOUNT_ERROR_MESSAGE = "최대 금액을 초과하였습니다.";
    
    @NotNull
    private BigDecimal price;

    protected Money() {
    }

    private Money(final BigDecimal price) {
        validateMoney(price);
        this.price = price;
    }

    private static void validateMoney(final BigDecimal amount) {
        if (amount.compareTo(MIN) < 0) {
            throw new IllegalArgumentException(AMOUNT_CANNOT_BE_BELOW_ZERO_ERROR_MESSAGE);
        }
        if (amount.scale() > 2) {
            throw new IllegalArgumentException(AMOUNT_IS_NOT_IN_SCALE_TWO_ERROR_MESSAGE);
        }
        if (amount.compareTo(MAX) >= 0) {
            throw new IllegalArgumentException(AMOUNT_EXCEEDS_OEVR_MAX_AMOUNT_ERROR_MESSAGE);
        }
    }

    public static Money valueOf(final long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money valueOf(final BigDecimal amount) {
        return new Money(amount);
    }

    public Money add(final Money money) {
        return new Money(price.add(money.price));
    }

    public Money multiply(final long value) {
        return new Money(price.multiply(BigDecimal.valueOf(value)));
    }

    public boolean isBiggerThan(final Money money) {
        return price.compareTo(money.price) >= 0;
    }

    @Override
    public int compareTo(final Money o) {
        return price.compareTo(o.price);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Money money = (Money) o;
        return Objects.equals(price, money.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    public BigDecimal getPrice() {
        return price;
    }
}
