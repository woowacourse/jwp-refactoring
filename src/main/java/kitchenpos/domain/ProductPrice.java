package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class ProductPrice {
    private BigDecimal price;

    public ProductPrice() {
    }

    public ProductPrice(final BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
