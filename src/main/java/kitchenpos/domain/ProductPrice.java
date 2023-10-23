package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ProductPrice {
    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;
    private static final BigDecimal MAX_PRICE = BigDecimal.valueOf(Math.pow(10, 17));
    
    private BigDecimal price;

    public ProductPrice() {
    }

    public ProductPrice(final BigDecimal price) {
        validation(price);
        this.price = price;
    }

    private void validation(final BigDecimal price) {
        if (Objects.isNull(price) || isLessThanMinPrice(price) || isMoreThanMaxPrice(price)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isMoreThanMaxPrice(final BigDecimal price) {
        return price.compareTo(MAX_PRICE) >= 0;
    }

    private boolean isLessThanMinPrice(final BigDecimal price) {
        return price.compareTo(MIN_PRICE) < 0;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
