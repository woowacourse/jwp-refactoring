package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private BigDecimal price;

    public Price() {
    }

    public Price(BigDecimal price) {
        validatePriceIsNotNullOrNotNegative(price);
        this.price = price;
    }

    private void validatePriceIsNotNullOrNotNegative(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal value() {
        return price;
    }
}
