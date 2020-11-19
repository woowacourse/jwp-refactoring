package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {
    private static final int MIN_PRICE = 0;

    private BigDecimal price;

    protected Price() {
    }

    public Price(Integer price) {
        this(BigDecimal.valueOf(price));
    }

    public Price(BigDecimal price) {
        validateGreaterOrSameThanZero(price);
        this.price = price;
    }

    private void validateGreaterOrSameThanZero(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < MIN_PRICE) {
            throw new IllegalArgumentException("price가 null이거나 0보다 작을 수 없습니다.");
        }
    }

    public boolean isSmaller(BigDecimal totalPrice) {
        return this.price.compareTo(totalPrice) > 0;
    }

    public BigDecimal multiply(Long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getPrice() {
        return price;
    }
}
