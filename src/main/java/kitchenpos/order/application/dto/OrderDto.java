package kitchenpos.order.application.dto;

import kitchenpos.order.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItems;

    private OrderDto(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemDto> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderDto from(Order order) {
        Long id = order.getId();
        Long orderTableId = order.getOrderTableId();
        String orderStatus = order.getOrderStatus().name();
        LocalDateTime orderedTime = order.getOrderedTime();
        List<OrderLineItemDto> orderLineItems = order.getOrderLineItems().stream()
                .map(OrderLineItemDto::from)
                .collect(Collectors.toList());
        return new OrderDto(id, orderTableId, orderStatus, orderedTime, orderLineItems);
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
