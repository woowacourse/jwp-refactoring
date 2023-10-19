package kitchenpos.domain.menu;

import java.math.BigDecimal;

public class MenuPrice {

    private final BigDecimal value;

    public MenuPrice(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴의 가격은 0원 이상이어야 한다.");
        }
        this.value = value;
    }

    public boolean isBigger(BigDecimal price) {
        return value.compareTo(price) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }
}
