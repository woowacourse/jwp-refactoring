package kitchenpos.domain;

import kitchenpos.exception.price.InvalidePriceValueException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal price) {
        this.price = price;
    }

    public static Price from(Long price) {
        validate(price);
        return new Price(BigDecimal.valueOf(price));
    }

    public static void validate(Long price) {
        if (Objects.isNull(price) || price < 0) {
            throw new InvalidePriceValueException();
        }
    }

    public Long longValue() {
        return price.longValue();
    }
}
