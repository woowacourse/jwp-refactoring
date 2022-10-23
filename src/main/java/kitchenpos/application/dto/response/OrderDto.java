package kitchenpos.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderDto {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemDto> orderLineItems;

    public OrderDto(Long id,
                    Long orderTableId,
                    String orderStatus,
                    LocalDateTime orderedTime,
                    List<OrderLineItemDto> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderDto of(Order order, List<OrderLineItem> orderLineItems) {
        Long id = order.getId();
        Long orderTableId = order.getOrderTableId();
        String orderStatus = order.getOrderStatus();
        LocalDateTime orderedTime = order.getOrderedTime();
        return new OrderDto(id, orderTableId, orderStatus, orderedTime, toOrderLineItemDtos(orderLineItems));
    }

    private static List<OrderLineItemDto> toOrderLineItemDtos(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemDto::of)
                .collect(Collectors.toList());
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
