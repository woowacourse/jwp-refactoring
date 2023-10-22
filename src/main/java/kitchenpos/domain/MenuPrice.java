package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import org.springframework.data.relational.core.mapping.Column;

public class MenuPrice {

    @Column("price")
    private final BigDecimal value;

    public MenuPrice(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(final BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    public boolean isSamePrice(final MenuPrice otherMenuPrice) {
        return value.compareTo(otherMenuPrice.value) == 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MenuPrice menuPrice = (MenuPrice) o;
        return Objects.equals(value, menuPrice.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
