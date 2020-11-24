package kitchenpos.domain.menu;

import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.util.ValidateUtil;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class MenuPrice {
    @Column(name = "price")
    private BigDecimal value;

    protected MenuPrice() {
    }

    private MenuPrice(BigDecimal value) {
        this.value = value;
    }

    public static MenuPrice of(BigDecimal value, BigDecimal menuProductPriceSum) {
        ValidateUtil.validateNonNull(value, menuProductPriceSum);
        validateValue(value);
        validateValueIsSmallerThanMenuProductPriceSum(value, menuProductPriceSum);

        return new MenuPrice(value);
    }

    private static void validateValue(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMenuPriceException("메뉴의 가격은 0원 이상이어야 합니다!");
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
