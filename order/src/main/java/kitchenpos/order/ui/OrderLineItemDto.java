package kitchenpos.order.ui;

import kitchenpos.order.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemDto {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemDto(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemDto> of(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemDto::from)
                .collect(Collectors.toList());
    }

    public static OrderLineItemDto from(OrderLineItem orderLineItem) {
        return new OrderLineItemDto(orderLineItem.getSeq(), orderLineItem.getOrderId(),
                orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
