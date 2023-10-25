package kitchenpos.ui.dto.response;

import kitchenpos.application.dto.ReadOrderDto;
import kitchenpos.application.dto.ReadOrderLineItemDto;

public class ReadOrderLineItemResponse {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public ReadOrderLineItemResponse(
            final ReadOrderDto readOrderDto,
            final ReadOrderLineItemDto readOrderLineItemDto
    ) {
        this.seq = readOrderLineItemDto.getSeq();
        this.orderId = readOrderDto.getId();
        this.menuId = readOrderLineItemDto.getMenuId();
        this.quantity = readOrderLineItemDto.getQuantity();
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
