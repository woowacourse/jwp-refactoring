package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;

public class OrderedMenu {

    private Long menuId;
    private String menuName;
    private BigDecimal menuPrice;

    protected OrderedMenu() {
    }

    private OrderedMenu(Long menuId, String menuName, BigDecimal menuPrice) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public static OrderedMenu from(Menu menu) {
        return new OrderedMenu(menu.getId(), menu.getName(), menu.getPrice());
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
