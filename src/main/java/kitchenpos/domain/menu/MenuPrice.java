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

    private MenuPrice(BigDecimal value) {
        this.value = value;
    }

    public static MenuPrice from(BigDecimal value) {
        validateValue(value);
        return new MenuPrice(value);
    }

    private static void validateValue(BigDecimal value) {
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
