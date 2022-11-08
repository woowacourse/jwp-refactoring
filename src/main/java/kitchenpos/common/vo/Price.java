package kitchenpos.common.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private BigDecimal price;

    protected Price() {
    }

    private Price(final BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public static Price valueOf(final BigDecimal value) {
        return new Price(value);
    }

    private void validate(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isBiggerThan(final BigDecimal number) {
        return price.compareTo(number) > 0;
    }

    public BigDecimal multiply(final long number) {
        return price.multiply(BigDecimal.valueOf(number));
    }

    public BigDecimal getPrice() {
        return price;
    }
}
