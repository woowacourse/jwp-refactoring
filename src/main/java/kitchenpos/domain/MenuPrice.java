package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidPriceException;

@Embeddable
public class MenuPrice {

    @Column
    private BigDecimal price;

    public MenuPrice() {
    }

    public MenuPrice(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new InvalidPriceException();
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException();
        }
    }

    public boolean isBigger(BigDecimal value) {
        return this.price.compareTo(value) > 0;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
