package kitchenpos.application.dto.request;

import java.util.List;

public class OrdersRequestDto {

    private Long orderTableId;
    private List<OrderLineItemRequestDto> orderLineItems;

    private OrdersRequestDto() {
    }

    public OrdersRequestDto(Long orderTableId, List<OrderLineItemRequestDto> orderLineItems) {
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
