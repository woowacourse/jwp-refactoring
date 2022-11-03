package kitchenpos.domain.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price")
    private long value;

    protected Price() {
    }

    public Price(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public Price add(Price additionalPrice) {
        return new Price(value + additionalPrice.value);
    }

    public boolean isExpensiveThan(Price sumPrice) {
        return value > sumPrice.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price = (Price) o;
        return value == price.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
