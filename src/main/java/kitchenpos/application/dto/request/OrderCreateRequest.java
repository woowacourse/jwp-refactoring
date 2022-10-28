package kitchenpos.application.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderCreateRequest {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItemsDto;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long id,
                              final Long orderTableId,
                              final String orderStatus,
                              final LocalDateTime orderedTime,
                              final List<OrderLineItemDto> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemsDto = orderLineItems;
    }

    public Order toOrder(final Long orderTableId,
                         final OrderStatus orderStatus,
                         final LocalDateTime orderedTime) {
        return new Order(
                id,
                orderTableId,
                orderStatus,
                orderedTime,
                toOrderLineItems()
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

    public List<OrderLineItemDto> getOrderLineItemsDto() {
        return orderLineItemsDto;
    }

    private List<OrderLineItem> toOrderLineItems() {
        return this.orderLineItemsDto.stream()
                .map(OrderLineItemDto::toOrderLineItem)
                .collect(Collectors.toList());
    }
}
