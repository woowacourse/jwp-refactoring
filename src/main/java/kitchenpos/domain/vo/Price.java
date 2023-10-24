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
            throw new IllegalArgumentException("가격은 음수일 수 없습니다.");
        }
        this.value = value;
    }

    public BigDecimal multiply(Quantity quantity) {
        return this.value.multiply(BigDecimal.valueOf(quantity.getValue()));
    }

    public BigDecimal getValue() {
        return value;
    }
}
