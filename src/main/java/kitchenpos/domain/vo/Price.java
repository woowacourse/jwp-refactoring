package kitchenpos.domain.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.badrequest.PriceInvalidException;

@Embeddable
public class Price {
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    private Price(final BigDecimal price) {
        if (Objects.isNull(price) || isLessThanZero(price)) {
            throw new PriceInvalidException();
        }

        this.price = price;
    }

    public static Price from(final BigDecimal price) {
        return new Price(price);
    }

    public static Price from(final String price) {
        return Price.from(new BigDecimal(price));
    }

    public boolean isLessThanZero() {
        return this.price.compareTo(BigDecimal.ZERO) < 0;
    }

    private boolean isLessThanZero(final BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    public BigDecimal getValue() {
        return price;
    }

    public Price multiply(final long quantity) {
        return new Price(this.price.multiply(BigDecimal.valueOf(quantity)));
    }
}
