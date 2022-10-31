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
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    public Price multiply(int operand) {
        return new Price(value.multiply(BigDecimal.valueOf(operand)));
    }

    public BigDecimal getValue() {
        return value;
    }
}
