package kitchenpos.domain;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class Price {

    private final BigDecimal value;

    public Price(BigDecimal value) {
        if (value == null || value.doubleValue() < 0) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    public Price multiply(int operand) {
        return new Price(value.multiply(BigDecimal.valueOf(operand)));
    }
}
