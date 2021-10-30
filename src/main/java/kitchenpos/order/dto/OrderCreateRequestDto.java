package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderCreateRequestDto {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItems = new ArrayList<>();

    public OrderCreateRequestDto() {
    }

    public OrderCreateRequestDto(Order order) {
        this.id = order.getId();
        this.orderTableId = order.getOrderTableId();
        this.orderStatus = order.getOrderStatus();
        this.orderedTime = order.getOrderedTime();
        for (OrderLineItem orderLineItem : order.getOrderLineItems()) {
            orderLineItems.add(new OrderLineItemDto(orderLineItem.getMenuId(), orderLineItem.getQuantity()));
        }
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

    public Order toEntity() {
        List<OrderLineItem> orderLineItemGroup = new ArrayList<>();
        for (OrderLineItemDto orderLineItemDto : orderLineItems) {
            orderLineItemGroup.add(new OrderLineItem(orderLineItemDto.getMenuId(), orderLineItemDto.getQuantity()));
        }
        return new Order(
            orderTableId,
            orderStatus,
            orderedTime,
            orderLineItemGroup);
    }
}
