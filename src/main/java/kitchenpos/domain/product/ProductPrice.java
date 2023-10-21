package kitchenpos.domain.product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import javax.persistence.Embeddable;
import kitchenpos.exception.ProductException.NegativePriceException;
import kitchenpos.exception.ProductException.NoPriceException;

@Embeddable
public class ProductPrice {

    private final BigDecimal price;

    public ProductPrice() {
        price = null;
    }

    public ProductPrice(final BigDecimal price) {
        validatePrice(price);
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    private void validatePrice(final BigDecimal price) {
        validateNotNull(price);
        validateNotNegative(price);
    }

    private void validateNotNull(final BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new NoPriceException();
        }
    }

    private void validateNotNegative(final BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativePriceException(price.longValue());
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
