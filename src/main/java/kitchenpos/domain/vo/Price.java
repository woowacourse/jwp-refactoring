package kitchenpos.domain.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    private Price(BigDecimal value) {
        this.value = value;
    }

    public static Price from(BigDecimal value) {
        validateNullOrNegative(value);

        return new Price(value);
    }

    private static void validateNullOrNegative(BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getValue() {
        return value;
    }

}
