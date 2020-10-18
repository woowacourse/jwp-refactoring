package kitchenpos.dto.order;

import kitchenpos.domain.OrderStatus;

import java.util.List;

public class OrderRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemDto> orderLineItemDtos;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemDto> orderLineItemDtos) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItemDtos = orderLineItemDtos;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemDto> getOrderLineItemDtos() {
        return orderLineItemDtos;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
