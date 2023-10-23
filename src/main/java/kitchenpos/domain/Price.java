package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidRequestParameterException;

@Embeddable
public class Price {

    @Column
    private BigDecimal price;

    private Price(BigDecimal price) {
        this.price = price;
    }

    protected Price() {
    }

    public static Price from(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRequestParameterException();
        }
        return new Price(price);
    }

    public boolean isLessThan(BigDecimal price) {
        return this.price.compareTo(price) > 0;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
