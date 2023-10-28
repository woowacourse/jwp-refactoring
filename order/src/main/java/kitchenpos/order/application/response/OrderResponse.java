package kitchenpos.order.application.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.dto.OrderLineItemDto;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItemDtos;

    protected OrderResponse() {
    }

    private OrderResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                         List<OrderLineItemDto> orderLineItemDtos) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemDtos = orderLineItemDtos;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                order.getOrderLineItems().stream()
                        .map(OrderLineItemDto::from)
                        .collect(Collectors.toList())
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

    public List<OrderLineItemDto> getOrderLineItemDtos() {
        return orderLineItemDtos;
    }
}
