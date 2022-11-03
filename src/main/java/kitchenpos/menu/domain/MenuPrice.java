package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.IllegalPriceException;
import kitchenpos.exception.MenuTotalPriceException;

@Embeddable
public class MenuPrice {

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected MenuPrice() {
    }

    public MenuPrice(final BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    private void validatePrice(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalPriceException();
        }
    }

    public void validatePriceToTotalProductPrice(final BigDecimal value) {
        if (this.value.compareTo(value) > 0) {
            throw new MenuTotalPriceException();
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
