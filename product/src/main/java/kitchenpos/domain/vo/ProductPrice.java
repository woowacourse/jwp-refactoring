package kitchenpos.domain.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.badrequest.PriceInvalidException;

@Embeddable
public class ProductPrice {
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    protected ProductPrice() {
    }

    private ProductPrice(final BigDecimal price) {
        if (Objects.isNull(price) || isLessThanZero(price)) {
            throw new PriceInvalidException();
        }

        this.price = price;
    }

    public static ProductPrice from(final BigDecimal price) {
        return new ProductPrice(price);
    }

    public static ProductPrice from(final String price) {
        return ProductPrice.from(new BigDecimal(price));
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

    public ProductPrice multiply(final long quantity) {
        return new ProductPrice(this.price.multiply(BigDecimal.valueOf(quantity)));
    }
}
