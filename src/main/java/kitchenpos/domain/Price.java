package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    private Price(final BigDecimal price) {
        validatePriceNotNull(price);
        validatePositivePrice(price);
        this.price = price.setScale(2);
    }

    private void validatePositivePrice(final BigDecimal price) {
        validatePriceNotNull(price);
        if (price.signum() < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
    }

    private void validatePriceNotNull(final BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("가격은 NULL이 될 수 없습니다.");
        }
    }

    public static Price of(final int price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public static Price of(final BigDecimal price) {
        return new Price(price);
    }

    public BigDecimal getPrice() {
        return price;
    }

    protected Price() {
    }
}
