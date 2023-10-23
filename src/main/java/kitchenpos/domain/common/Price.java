package kitchenpos.domain.common;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    private static final Price ZERO = new Price(BigDecimal.ZERO);

    private BigDecimal price;

    protected Price() {
    }

    public Price(final BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || isUnderZero(price)) {
            throw new IllegalArgumentException(String.format("가격은 null 또는 0원 보다 작을 수 없습니다. 입력값 = %s", price));
        }
    }

    private boolean isUnderZero(final BigDecimal price) {
        return BigDecimal.ZERO.compareTo(price) > 0;
    }

    public static Price zero() {
        return ZERO;
    }

    public Price add(final BigDecimal price) {
        return new Price(this.price.add(price));
    }

    public int compareTo(final Price target) {
        return price.compareTo(target.getPrice());
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
