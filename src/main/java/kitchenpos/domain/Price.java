package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {
    private static final int MIN_PRICE = 0;
    public static final Price ZERO = new Price(0);

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

    public boolean isSmaller(Price totalPrice) {
        return this.price.compareTo(totalPrice.price) > 0;
    }

    public Price multiply(Long quantity) {
        BigDecimal result = price.multiply(BigDecimal.valueOf(quantity));
        return new Price(result);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Price sum(Price calculateAmount) {
        BigDecimal result = this.price.add(calculateAmount.price);
        return new Price(result);
    }
}
