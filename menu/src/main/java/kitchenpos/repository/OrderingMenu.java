package kitchenpos.repository;

import java.math.BigDecimal;
import kitchenpos.domain.vo.MenuPrice;

public class OrderingMenu {
    private Long menuId;
    private String menuName;
    private BigDecimal menuPrice;
    private String menuGroupName;

    protected OrderingMenu() {
    }

    public OrderingMenu(final Long menuId, final String menuName, final MenuPrice menuPrice,
                        final String menuGroupName) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice.getValue();
        this.menuGroupName = menuGroupName;
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
