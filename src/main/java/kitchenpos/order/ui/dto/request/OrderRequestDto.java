package kitchenpos.order.ui.dto.request;

import java.util.List;

public class OrderRequestDto {

    private Long orderTableId;
    private List<OrderLineItemRequestDto> orderLineItems;

    private OrderRequestDto() {
    }

    public OrderRequestDto(Long orderTableId,
        List<OrderLineItemRequestDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequestDto> getOrderLineItems() {
        return orderLineItems;
    }
}
