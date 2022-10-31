package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price")
    private BigDecimal value;

    protected Price() {
    }

    public Price(BigDecimal value) {
        if (value == null || value.doubleValue() < 0) {
            throw new IllegalArgumentException("가격은 0 이상의 값이어야 합니다.");
        }
        this.value = value;
    }

    public boolean isBiggerThan(Price target) {
        return value.compareTo(target.value) > 0;
    }

    public Price multiply(int operand) {
        return new Price(value.multiply(BigDecimal.valueOf(operand)));
    }

    public BigDecimal getValue() {
        return value;
    }
}
