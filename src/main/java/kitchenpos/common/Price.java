package kitchenpos.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    @Column
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("올바르지 않은 가격입니다.");
        }
        this.price = price;
    }

    public Price(long price) {
        this(BigDecimal.valueOf(price));
    }

    public Price(double price) {
        this(BigDecimal.valueOf(price));
    }

    public BigDecimal getPrice() {
        return price;
    }
}
