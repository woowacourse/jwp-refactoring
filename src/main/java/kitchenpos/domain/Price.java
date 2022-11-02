package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price")
    private BigDecimal amount;

    protected Price() {
    }

    public Price(final BigDecimal amount) {
        validateValidAmount(amount);
        this.amount = amount;
    }

    private void validateValidAmount(final BigDecimal amount) {
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void validateLowerThan(final BigDecimal totalPrice) {
        if (amount.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Price)) {
            return false;
        }
        final Price price = (Price) o;
        return Objects.equals(amount, price.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
