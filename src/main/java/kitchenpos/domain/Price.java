package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    private BigDecimal value;

    protected Price() {
    }

    public Price(BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격이 0원 미만일 경우 생성할 수 없습니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    public BigDecimal multiply(long quantity) {
        return value.multiply(BigDecimal.valueOf(quantity));
    }
}
