package kitchenpos.domain;

import java.math.BigDecimal;

public class OrderMenu {

    private final Long menuId;
    private final String menuName;
    private final BigDecimal menuPrice;

    private OrderMenu(final Long menuId, final String menuName, final BigDecimal menuPrice) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public static OrderMenu from(final Menu menu) {
        return new OrderMenu(
                menu.getId(),
                menu.getName(),
                menu.getPrice()
        );
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }
}
