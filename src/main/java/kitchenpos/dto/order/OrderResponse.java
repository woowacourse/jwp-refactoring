package kitchenpos.dto.order;

import kitchenpos.domain.Order;

import java.util.List;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemDto> orderLineItemDtos;

    public OrderResponse(Long id, Long orderTableId, String orderStatus, List<OrderLineItemDto> orderLineItemDtos) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItemDtos = orderLineItemDtos;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTable().getId(),
                order.getOrderStatus(),
                OrderLineItemDto.listOf(order.getOrderLineItems())
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemDto> getOrderLineItemDtos() {
        return orderLineItemDtos;
    }
}
