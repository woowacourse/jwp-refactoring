package kitchenpos.order.domain;

import java.math.BigDecimal;


public class OrderMenu {

    private Long menuId;
    private String name;
    private BigDecimal price;

    protected OrderMenu() {
    }

    public OrderMenu(Long menuId, String name, BigDecimal price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
