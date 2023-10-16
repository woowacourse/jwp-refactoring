package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price")
    private BigDecimal value;

    protected Price() {
    }

    public Price(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(BigDecimal value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException();
        }
        if (BigDecimal.ZERO.compareTo(value) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal value() {
        return value;
    }
}
