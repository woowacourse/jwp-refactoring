package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    protected Price() {
    }

    private Price(final BigDecimal price) {
        this.price = price;
    }

    public static Price from(final BigDecimal price) {
        validatePrice(price);
        return new Price(price);
    }

    public static Price createZero() {
        return new Price(BigDecimal.ZERO);
    }

    private static void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("[ERROR] 금액이 없거나, 음수입니다.");
        }
    }

    public Price plus(final Price price) {
        return new Price(this.price.add(price.price));
    }

    public Price multiply(final Long number) {
        final BigDecimal convertNumber = BigDecimal.valueOf(number);
        return new Price(this.price.multiply(convertNumber));
    }

    public boolean isGreaterThan(final Price another) {
        return this.price.compareTo(another.price) > 0;
    }

    public BigDecimal getPrice() {
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
}
