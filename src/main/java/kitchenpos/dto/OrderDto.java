package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;

public class OrderDto {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemDto> orderLineItems;

    public OrderDto(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                    final List<OrderLineItemDto> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderDto toDto(final Order order) {
        final List<OrderLineItemDto> orderLineItemDtos = order.getOrderLineItems()
                .stream()
                .map(OrderLineItemDto::toDto)
                .collect(Collectors.toList());
        return new OrderDto(order.getId(), order.getOrderTable().getId(), order.getOrderStatus().name(), order.getOrderedTime(),
                orderLineItemDtos);
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
}
