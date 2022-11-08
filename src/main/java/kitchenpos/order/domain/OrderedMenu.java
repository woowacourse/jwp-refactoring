package kitchenpos.order.domain;

import java.math.BigDecimal;

public class OrderedMenu {

    private Long id;
    private Long menuId;
    private String name;
    private BigDecimal price;

    public OrderedMenu() {
    }

    public OrderedMenu(final Long menuId, final String name, final BigDecimal price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public OrderedMenu(final Long id, final Long menuId, final String name, final BigDecimal price) {
        this.id = id;
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
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
