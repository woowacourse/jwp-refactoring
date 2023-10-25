package kitchenpos.domain.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class Price {

    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    public Price(final int price) {
        final BigDecimal wrappedPrice = BigDecimal.valueOf(price);
        validate(wrappedPrice);
        this.price = wrappedPrice;
    }

    public Price(final BigDecimal price) {
        validate(price);
        this.price = price;
    }

    private void validate(final BigDecimal price) {
        if (Objects.isNull(price) || isLowerThanZero(price)) {
            throw new IllegalArgumentException("올바르지 않은 가격입니다.");
        }
    }

    private boolean isLowerThanZero(final BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    public BigDecimal value() {
        return price;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    public boolean isHigherThan(final Price other) {
        return this.price.compareTo(other.price) > 0;
    }

    public Price multiply(final long count) {
        return new Price(price.multiply(BigDecimal.valueOf(count)));
    }

    public Price add(final Price price) {
        return new Price(this.price.add(price.price));
    }
}
