package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price")
    private BigDecimal value;

    public Price() {
    }

    public Price(final BigDecimal value) {
        validates(value);
        this.value = value;
    }

    private static void validates(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(String.format("가격은 0이상 입니다.(price: %s)", value));
        }
    }

    public boolean isBiggerThan(final BigDecimal target) {
        return value.compareTo(target) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }
}
