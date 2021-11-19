package kitchenpos.ui.dto.request.order;

import java.util.List;

public class OrderRequestDto {

    private Long orderTableId;
    private List<OrderLineItemDto> orderLineItems;

    private OrderRequestDto() {
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }
}
