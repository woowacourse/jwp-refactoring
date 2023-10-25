package kitchenpos.ui.dto.response;

import kitchenpos.application.dto.ChangeOrderDto;
import kitchenpos.application.dto.ChangeOrderLineItemDto;

public class UpdateOrderLineItemResponse {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public UpdateOrderLineItemResponse(
            final ChangeOrderDto changeOrderDto,
            final ChangeOrderLineItemDto changeOrderLineItemDto
    ) {
        this.seq = changeOrderLineItemDto.getSeq();
        this.orderId = changeOrderDto.getId();
        this.menuId = changeOrderLineItemDto.getMenuId();
        this.quantity = changeOrderLineItemDto.getQuantity();
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
