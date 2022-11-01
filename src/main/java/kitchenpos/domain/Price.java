package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    public static final Price ZERO = new Price(BigDecimal.ZERO);

    @Column(name = "price", nullable = false)
    private BigDecimal amount;

    protected Price() {
    }

    public Price(BigDecimal amount) {
        validateZeroOrPositive(amount);
        this.amount = amount;
    }

    private void validateZeroOrPositive(BigDecimal amount) {
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
    }

    public Price add(Price other) {
        return new Price(this.amount.add(other.amount));
    }

    public boolean isMoreExpensiveThan(Price other) {
        return this.amount.compareTo(other.amount) > 0;
    }

    public Price multiply(Quantity quantity) {
        return new Price(amount.multiply(BigDecimal.valueOf(quantity.getVolume())));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price = (Price) o;
        return Objects.equals(getAmount(), price.getAmount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAmount());
    }
}
