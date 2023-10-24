package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private static final BigDecimal MIN = BigDecimal.ZERO;
    public static final Price ZERO = Price.from(BigDecimal.ZERO);

    @Column
    private BigDecimal price;

    protected Price() {}

    private Price(final BigDecimal price) {
        this.price = price;
    }

    public static Price from(final BigDecimal value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("가격은 비어있을 수 없습니다.");
        }
        if (value.compareTo(MIN) < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
        return new Price(value);
    }

    public boolean isGreaterThan(final Price other) {
        return this.price.compareTo(other.price) > 0;
    }

    public Price multiply(final BigDecimal other) {
        return Price.from(this.price.multiply(other));
    }

    public Price add(final Price other) {
        return Price.from(this.price.add(other.price));
    }

    public BigDecimal getPrice() {
        return price;
    }
}
