package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(value = AccessType.FIELD)
public class Price implements Comparable<Price> {

    private BigDecimal price;

    protected Price() {
    }

    private Price(final BigDecimal price) {
        this.price = price;
    }

    public static Price from(BigDecimal price) {
        validate(price);
        return new Price(price);
    }

    private static void validate(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0보다 작을 수 없습니다.");
        }
    }

    public Price add(Price other) {
        return from(price.add(other.price));
    }

    public Price multiply(Price other) {
        return from(price.multiply(other.price));
    }

    public Price multiply(long quantity) {
        return from(price.multiply(BigDecimal.valueOf(quantity)));
    }

    @Override
    public int compareTo(final Price other) {
        return price.compareTo(other.price);
    }

    public BigDecimal getPrice() {
        return price;
    }
}
