package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.PriceException;

@Embeddable
public class Price {

    @Column(name = "price")
    private BigDecimal value;

    public Price() {
        value = BigDecimal.ZERO;
    }

    public Price(BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new PriceException();
        }
    }

    public void multiply(long count) {
        value = value.multiply(BigDecimal.valueOf(count));
    }

    public void add(Price price) {
        value = value.add(price.value);
    }

    public boolean isExpensiveThan(Price price) {
        return value.compareTo(price.value) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }
}
