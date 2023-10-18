package kitchenpos.domain.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;

public class Price {

    @Column(name = "price", precision = 19, scale = 0)
    private BigDecimal value;

    protected Price() {
    }

    public Price(BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
