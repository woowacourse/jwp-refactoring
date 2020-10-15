package kitchenpos.dto.order;

import java.util.List;

public class OrderCreateRequest {
    private Long orderTableId;
    private List<OrderLineItemDto> orderLineItemDtos;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemDto> orderLineItemDtos) {
        this.orderTableId = orderTableId;
        this.orderLineItemDtos = orderLineItemDtos;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemDto> getOrderLineItemDtos() {
        return orderLineItemDtos;
    }
}
