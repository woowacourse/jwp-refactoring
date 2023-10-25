package kitchenpos.ui.dto.response;

import kitchenpos.application.dto.CreateOrderDto;
import kitchenpos.application.dto.CreateOrderLineItemDto;

public class CreateOrderLineItemResponse {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public CreateOrderLineItemResponse(
            final CreateOrderDto createOrderDto,
            final CreateOrderLineItemDto orderLineItem
    ) {
        this.seq = orderLineItem.getSeq();
        this.orderId = createOrderDto.getId();
        this.menuId = orderLineItem.getMenuId();
        this.quantity = orderLineItem.getQuantity();
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
