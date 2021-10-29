package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    private final BigDecimal price;

    public Price(Long price) {
        validate(price);
        this.price = BigDecimal.valueOf(price);
    }

    public Price(BigDecimal price) {
        this(price.longValue());
    }

    private void validate(Long value) {
        if (Objects.isNull(value) || value.compareTo(0L) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
