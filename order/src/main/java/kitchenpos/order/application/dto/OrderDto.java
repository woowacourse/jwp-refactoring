package kitchenpos.order.application.dto;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;

public class OrderDto {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemDto> orderLineItems;

    public OrderDto(
        final Long id,
        final Long orderTableId,
        final String orderStatus,
        final LocalDateTime orderedTime,
        final List<OrderLineItemDto> orderLineItems
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderDto from(final Order savedOrder) {
        final List<OrderLineItemDto> orderLineItemDtos = savedOrder.getOrderLineItems()
            .stream()
            .map(orderLineItem -> OrderLineItemDto.of(orderLineItem, savedOrder.getId()))
            .collect(toUnmodifiableList());

        return new OrderDto(
            savedOrder.getId(),
            savedOrder.getOrderTableId(),
            savedOrder.getOrderStatus().name(),
            savedOrder.getOrderedTime(),
            orderLineItemDtos
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
            "id=" + id +
            ", orderTableId=" + orderTableId +
            ", orderStatus='" + orderStatus + '\'' +
            ", orderedTime=" + orderedTime +
            ", orderLineItems=" + orderLineItems +
            '}';
    }
}
