package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private BigDecimal price;

    public Price() {
    }

    public Price(Long price) {
        if (price == null || price < 0) {
            throw new IllegalArgumentException();
        }
        this.price = BigDecimal.valueOf(price);
    }

    public BigDecimal getPrice() {
        return price;
    }
}
