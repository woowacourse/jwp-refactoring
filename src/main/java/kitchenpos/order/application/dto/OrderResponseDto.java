package kitchenpos.order.application.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.order.model.Order;
import kitchenpos.orderline.model.OrderLineItem;
import kitchenpos.orderline.application.dto.OrderLineItemResponseDto;

public class OrderResponseDto {
    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponseDto> orderLineResponses;

    public static OrderResponseDto from(Order order, List<OrderLineItem> orderLineItems) {
        List<OrderLineItemResponseDto> orderLineItemResponses = OrderLineItemResponseDto.listOf(orderLineItems);
        return new OrderResponseDto(order.getId(), order.getOrderTableId(), order.getOrderStatus().name(),
            order.getOrderedTime(), orderLineItemResponses);
    }

    public OrderResponseDto(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
        List<OrderLineItemResponseDto> orderLineResponses) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineResponses = orderLineResponses;
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponseDto> getOrderLineResponses() {
        return orderLineResponses;
    }
}
