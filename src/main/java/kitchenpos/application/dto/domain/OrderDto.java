package kitchenpos.application.dto.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;

public class OrderDto {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private String orderedTime;
    private List<OrderLineItemDto> orderLineItems;

    public static OrderDto from(final Order order) {
        List<OrderLineItemDto> orderLineItemDtos = order.getOrderLineItems().stream()
                .map(orderLineItem -> OrderLineItemDto.from(order.getId(), orderLineItem))
                .collect(Collectors.toList());

        return new OrderDto(order.getId(), order.getOrderTableId(), order.getOrderStatus().name(),
                order.getOrderedTime().toString(), orderLineItemDtos);
    }

    public OrderDto(final Long id, final Long orderTableId, final String orderStatus, final String orderedTime,
                    final List<OrderLineItemDto> orderLineItems) {
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

    public String getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }

}
