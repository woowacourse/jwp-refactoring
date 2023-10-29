package vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    public static final Price ZERO = new Price(BigDecimal.ZERO);
    private final BigDecimal price;

    protected Price() {
        price = null;
    }

    public Price(final BigDecimal price) {
        validatePrice(price);
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    private void validatePrice(final BigDecimal price) {
        validateNotNull(price);
        validateNotNegative(price);
    }

    private void validateNotNull(final BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("금액은 필수입니다.");
        }
    }

    private void validateNotNegative(final BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("금액은 양수여야합니다");
        }
    }

    public Price multiply(final Long rate) {
        return new Price(price.multiply(BigDecimal.valueOf(rate)));
    }

    public Price add(final Price price) {
        return new Price(this.price.add(price.price));
    }

    public boolean isMoreThan(final Price totalPrice) {
        return this.price.compareTo(totalPrice.price) > 0;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
