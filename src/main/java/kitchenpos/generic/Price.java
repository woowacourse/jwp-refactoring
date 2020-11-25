package kitchenpos.generic;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        this.price = price;
        validate();
    }

    public static Price of(Long price) {
        return new Price(BigDecimal.valueOf(price));
    }

    private void validate() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(String.format("%f : 가격은 0원 이상이어야 합니다.", price));
        }
    }

    public Price add(Price price) {
        return new Price(this.price.add(price.price));
    }

    public Price multiply(Long value) {
        return new Price(this.price.multiply(BigDecimal.valueOf(value)));
    }

    public boolean isLessThan(Price price) {
        return this.price.compareTo(price.price) < 0;
    }

    public Long longValue() {
        return this.price.longValue();
    }

    public BigDecimal bigDecimalValue() {
        return this.price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return price.compareTo(price1.price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
