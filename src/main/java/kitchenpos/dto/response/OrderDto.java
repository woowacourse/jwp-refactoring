package kitchenpos.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;

public class OrderDto {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItems;

    public static OrderDto from(Order savedOrder) {
        List<OrderLineItemDto> orderLineItemDtos = savedOrder.getOrderLineItems().stream()
                .map(OrderLineItemDto::from)
                .collect(Collectors.toList());
        return new OrderDto(savedOrder.getId(), savedOrder.getOrderTableId(),
                savedOrder.getOrderStatus(), savedOrder.getOrderedTime(), orderLineItemDtos);
    }

    public OrderDto(Long orderTableId, List<OrderLineItemDto> orderLineItems) {
        this(null, orderTableId, null, null, orderLineItems);
    }

    public OrderDto(Long orderTableId, String orderStatus, List<OrderLineItemDto> orderLineItems) {
        this(null, orderTableId, orderStatus, null, orderLineItems);
    }

    public OrderDto(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
            List<OrderLineItemDto> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
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
