package kitchenpos.order.domain;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Menu;

public class OrderMenu {

    private Long id;
    private Long menuId;
    private String name;
    private BigDecimal price;
    private String menuGroupName;
    private OrderProducts orderProducts;

    public OrderMenu(final Menu menu) {
        this.menuId = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.menuGroupName = menu.getMenuGroupName();
        this.orderProducts = OrderProducts.createByMenuProducts(menu.getMenuProducts());
    }

    public OrderMenu(final Long id, final String name, final BigDecimal price, final String menuGroupName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupName = menuGroupName;
    }

    public boolean hasSameMenuId(final Long menuId) {
        return this.menuId.equals(menuId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getMenuGroupName() {
        return menuGroupName;
    }

    public OrderProducts getOrderProducts() {
        return orderProducts;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public void setOrderProducts(final OrderProducts orderProducts) {
        this.orderProducts = orderProducts;
    }
}
