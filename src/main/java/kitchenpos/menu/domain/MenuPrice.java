package kitchenpos.menu.domain;

import javax.persistence.Column;
import java.math.BigDecimal;

public class MenuPrice {

    @Column(name = "price")
    private BigDecimal value;

    protected MenuPrice() {
    }

    private MenuPrice(final BigDecimal value) {
        validateMoreThanZero(value);
        this.value = value;
    }

    private void validateMoreThanZero(final BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public static MenuPrice of(final BigDecimal value) {
        return new MenuPrice(value);
    }

    public boolean isMoreThan(final MenuPrice otherPrice) {
        return value.compareTo(otherPrice.getValue()) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }
}
