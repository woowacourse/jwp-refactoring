package kitchenpos.domain;

import kitchenpos.exception.PriceEmptyException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Price {

    private static final int ZERO_PRICE = 0;

    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    private Price(final BigDecimal price) {
        this.price = price;
    }

    public static Price from(final Long price) {
        validateEmptyPrice(price);
        return new Price(BigDecimal.valueOf(price));
    }

    private static void validateEmptyPrice(final Long price) {
        if (price == null || price < ZERO_PRICE) {
            throw new PriceEmptyException();
        }
    }

    public Long getPrice() {
        return price.longValue();
    }
}
