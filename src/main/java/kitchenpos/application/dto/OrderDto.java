package kitchenpos.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;

public class OrderDto {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItems;

    private OrderDto() {
    }

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

    public static OrderDto of(Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();
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
