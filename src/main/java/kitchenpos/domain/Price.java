package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    public static Price ZERO = new Price(BigDecimal.ZERO);

    private BigDecimal amount;

    public Price() {
    }

    public Price(BigDecimal amount) {
        validateNotNull(amount);
        validateNotNegative(amount);
        this.amount = amount;
    }

    public Price(int amount) {
        this(new BigDecimal(amount));
    }

    public Price(String amount) {
        this(new BigDecimal(amount));
    }

    public Price add(Price other) {
        return new Price(amount.add(other.amount));
    }

    public Price multiply(long times) {
        validateNotNegative(times);
        BigDecimal multiplied = amount.multiply(new BigDecimal(times));
        return new Price(multiplied);
    }

    public boolean isEqualTo(Price other) {
        return amount.compareTo(other.amount) == 0;
    }

    public boolean isLessThan(Price other) {
        return amount.compareTo(other.amount) < 0;
    }

    public boolean isGreaterThan(Price other) {
        return amount.compareTo(other.amount) > 0;
    }

    private void validateNotNull(BigDecimal amount) {
        if (Objects.isNull(amount)) {
            throw new IllegalArgumentException("가격은 비워둘 수 없습니다");
        }
    }

    private void validateNotNegative(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0보다 작을 수 없습니다");
        }
    }

    private void validateNotNegative(long times) {
        if (times < 0) {
            throw new IllegalArgumentException("가격에 음수를 곱할 수 없습니다");
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
