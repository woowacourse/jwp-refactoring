package kitchenpos.order.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private static final String PRICE_ERROR_MESSAGE = "가격은 0 이상의 수여야 합니다.";

    @Column
    private BigDecimal price;

    public Price() {
    }

    public Price(final BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(PRICE_ERROR_MESSAGE);
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
