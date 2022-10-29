package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

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

    public boolean isMoreExpensiveThan(BigDecimal other) {
        return this.amount.compareTo(other) > 0;
    }

    public BigDecimal multiply(Quantity quantity) {
        return amount.multiply(BigDecimal.valueOf(quantity.getVolume()));
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
