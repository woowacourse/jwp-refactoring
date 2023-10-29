package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.PriceException;

@Embeddable
public class Price {

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price)
                || price.compareTo(BigDecimal.ZERO) < 0
                || price.compareTo(BigDecimal.valueOf(Math.pow(10, 20))) >= 0
        ) {
            throw new PriceException("가격이 유효하지 않습니다.");
        }
    }

    public Price multiply(long num) {
        return new Price(value.multiply(BigDecimal.valueOf(num)));
    }

    public Price plus(Price otherPrice) {
        return new Price(value.add(otherPrice.getValue()));
    }

    public boolean biggerThan(Price otherPrice) {
        return value.compareTo(otherPrice.getValue()) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price1 = (Price) o;
        return Objects.equals(value, price1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
