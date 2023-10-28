package kitchenpos.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price")
    private final BigDecimal value;

    @Deprecated
    protected Price() {
        value = null;
    }

    public Price(long value) {
        this(BigDecimal.valueOf(value));
    }

    public Price(double value) {
        this(BigDecimal.valueOf(value));
    }

    public Price(BigDecimal value) {
        checkNull(value);
        checkPositive(value);
        this.value = value;
    }

    private void checkNull(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("가격은 NULL일 수 없습니다.");
        }
    }

    private void checkPositive(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
    }

    public boolean isBiggerThan(Price other) {
        return this.value.compareTo(other.value) > 0;
    }

    public Price calculateAmount(Quantity quantity) {
        BigDecimal amount = value.multiply(new BigDecimal(quantity.value()));
        return new Price(amount);
    }

    public Price add(Price other) {
        return new Price(this.value.add(other.value));
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
