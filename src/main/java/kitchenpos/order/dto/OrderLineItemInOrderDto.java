package kitchenpos.order.dto;

import java.math.BigDecimal;

public class OrderLineItemInOrderDto {

    private final Long menuId;
    private final String menuName;
    private final BigDecimal menuPrice;
    private final long quantity;

    public OrderLineItemInOrderDto(
            final Long menuId,
            final String menuName,
            final BigDecimal menuPrice,
            final long quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
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

    public long getQuantity() {
        return quantity;
    }
}
