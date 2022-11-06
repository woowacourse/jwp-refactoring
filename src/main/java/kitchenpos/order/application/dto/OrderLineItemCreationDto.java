package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.ui.dto.request.OrderLineItemReeust;

public class OrderLineItemCreationDto {

    private final Long menuId;
    private final int quantity;

    private OrderLineItemCreationDto(final Long menuId, final int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemCreationDto from(final OrderLineItemReeust orderLineItemReeust) {
        return new OrderLineItemCreationDto(orderLineItemReeust.getMenuId(), orderLineItemReeust.getQuantity());
    }

    public static OrderLineItem toEntity(final OrderLineItemCreationDto orderLineItemCreationDto) {
        return new OrderLineItem(orderLineItemCreationDto.getQuantity());
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderLineItemCreationDto{" +
                "menuId=" + menuId +
                ", quantity=" + quantity +
                '}';
    }
}
