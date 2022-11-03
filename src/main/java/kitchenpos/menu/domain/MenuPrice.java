package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidPriceException;

@Embeddable
public class MenuPrice {

    private static final int PRICE_STANDARD = 0;

    @Column(name = "price", precision = 19, scale = 2, nullable = false)
    private BigDecimal value;

    public MenuPrice(final BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    private void validatePrice(final BigDecimal price) {
        if (isNotAllowedPrice(price)) {
            throw new InvalidPriceException("가격은 0보다 커야합니다.");
        }
    }

    private boolean isNotAllowedPrice(final BigDecimal price) {
        return Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < PRICE_STANDARD;
    }

    protected MenuPrice() {
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Price{" +
                "price=" + value +
                '}';
    }
}
