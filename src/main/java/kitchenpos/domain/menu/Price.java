package kitchenpos.domain.menu;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        this.price = price;
    }

    public Price(Long price) {
        this(BigDecimal.valueOf(price));
    }

    public BigDecimal getPrice() {
        return price;
    }
}
