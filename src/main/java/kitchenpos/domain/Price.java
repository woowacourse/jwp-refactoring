package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price")
    private BigDecimal value;

    public Price() {
    }

    public Price(final BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    public static Price ofMenu(final BigDecimal price, final ProductQuantities productQuantities) {
        BigDecimal sum = productQuantities.sum();
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
        return new Price(price);
    }

    private void validatePrice(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
