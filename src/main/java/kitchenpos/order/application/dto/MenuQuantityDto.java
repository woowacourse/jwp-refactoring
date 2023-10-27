package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderLineItem;

public class MenuQuantityDto {

    private Long menuId;
    private long quantity;

    private MenuQuantityDto() {}

    public MenuQuantityDto(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static MenuQuantityDto from(final OrderLineItem orderLineItem) {
        return new MenuQuantityDto(orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
