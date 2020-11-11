package kitchenpos.domain.menu;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    public static Price zero() {
        return new Price(BigDecimal.ZERO);
    }

    public static void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Price addTotalPrice(Price price, long quantity) {
        Price totalPrice = price.multiply(quantity);
        return new Price(this.price.add(totalPrice.getPrice()));
    }

    private Price multiply(long quantity) {
        return new Price(price.multiply(BigDecimal.valueOf(quantity)));
    }

    public boolean biggerThan(Price sum) {
        return this.price.compareTo(sum.getPrice()) > 0;
    }

    public BigDecimal getPrice() {
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
        Price price = (Price) o;
        return Objects.equals(this.price, price.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
