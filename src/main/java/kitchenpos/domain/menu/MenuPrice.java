package kitchenpos.domain.menu;

import kitchenpos.exception.InvalidMenuPriceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class MenuPrice {
    @Column(name = "price")
    private BigDecimal value;

    public MenuPrice() {
    }

    public MenuPrice(BigDecimal value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMenuPriceException("메뉴의 가격은 0원 이상이어야 합니다!");
        }
    }

    public boolean isBiggerThan(BigDecimal sum) {
        return this.value.compareTo(sum) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }
}
