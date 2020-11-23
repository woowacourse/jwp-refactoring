package kitchenpos.domain.menu;

import kitchenpos.exception.InvalidMenuPriceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class MenuPrice {
    private static final int MIN_MENU_PRICE = 0;

    @Column(name = "price")
    private BigDecimal value;

    protected MenuPrice() {
    }

    private MenuPrice(BigDecimal value) {
        this.value = value;
    }

    public static MenuPrice of(BigDecimal value, BigDecimal menuProductPriceSum) {
        validateValue(value);
        validateValueIsSmallerThanMenuProductPriceSum(value, menuProductPriceSum);

        return new MenuPrice(value);
    }

    private static void validateValue(BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < MIN_MENU_PRICE) {
            throw new InvalidMenuPriceException("메뉴의 가격은 " + MIN_MENU_PRICE + "원 이상이어야 합니다!");
        }
    }

    private static void validateValueIsSmallerThanMenuProductPriceSum(BigDecimal value, BigDecimal menuProductPriceSum) {
        if (0 < value.compareTo(menuProductPriceSum)) {
            throw new InvalidMenuPriceException("메뉴 가격은 구성된 상품 가격의 총합을 초과할 수 없습니다!");
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
