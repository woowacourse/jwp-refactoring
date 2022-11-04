package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private BigDecimal price;

    protected Price() {
    }

    public Price(final BigDecimal price) {
        validateCorrectPrice(price);
        this.price = new BigDecimal(price.intValue());
    }

    private void validateCorrectPrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0원 이상의 정수로 입력해주세요.");
        }
    }

    public boolean isExpensiveThan(final BigDecimal otherPrice) {
        return this.price.compareTo(otherPrice) > 0;
    }

    public BigDecimal multiply(final BigDecimal quantity) {
        return this.price.multiply(quantity);
    }

    public BigDecimal getPrice() {
        return new BigDecimal(price.intValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price that = (Price) o;
        return this.price.equals(that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
