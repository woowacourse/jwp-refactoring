package kitchenpos.domain;

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

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Price addWithProductQuantity(Price price, long quantity) {
        Price productPrice = price.multiply(quantity);
        return new Price(this.price.add(productPrice.getPrice()));
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
}
