package kitchenpos.domain;

import java.math.BigDecimal;

public class OrderMenu {

    private final Long menuId;
    private final String menuName;
    private final BigDecimal menuPrice;
    private final String menuGroupName;

    private OrderMenu(final Long menuId, final String menuName, final BigDecimal menuPrice, final String menuGroupName) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuGroupName = menuGroupName;
    }

    public static OrderMenu of(final Menu menu, final MenuGroup menuGroup) {
        return new OrderMenu(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menuGroup.getName()
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

    public String getMenuGroupName() {
        return menuGroupName;
    }
}
