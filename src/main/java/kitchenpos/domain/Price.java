package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidPriceException;

@Embeddable
public class Price {

    @Column
    private BigDecimal price;

    public Price() {
    }

    private Price(int price) {
        this.price = new BigDecimal(price);
    }

    public static Price create(int price) {
        validatePrice(price);
        return new Price(price);
    }

    private static void validatePrice(int price) {
        if(price < 0) {
            throw new InvalidPriceException();
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
