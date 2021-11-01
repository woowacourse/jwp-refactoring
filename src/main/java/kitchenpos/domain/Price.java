package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;
import kitchenpos.exception.KitchenException;

@Embeddable
public class Price {

    private BigDecimal price;

    protected Price() {

    }

    public Price(BigDecimal price) {
        this.price = price;
        validate();
    }

    public static Price of(long price) {
        return new Price(BigDecimal.valueOf(price));
    }

    private void validate() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new KitchenException("금액은 1원 이상이어야 합니다.");
        }
    }

    public Price add(Price other) {
        return new Price(this.price.add(other.price));
    }

    public Price multiply(long quantity) {
        return new Price(this.price.multiply(BigDecimal.valueOf(quantity)));
    }

    public boolean isLessThan(Price price) {
        return this.price.compareTo(price.price) < 0;
    }

    public BigDecimal bigDecimalValue() {
        return price;
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
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
