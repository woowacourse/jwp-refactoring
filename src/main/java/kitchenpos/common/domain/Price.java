package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column
    private BigDecimal price;

    public Price(final BigDecimal value) {
        validatePrice(value);
        this.price = value;
    }

    public Price() {
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 양수여야합니다.");
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
