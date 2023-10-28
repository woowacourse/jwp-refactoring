package kitchenpos.ordertable.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderLineItem;

public class OrderLineItemResponse {

    private final Long id;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public OrderLineItemResponse(Long id, Long orderId, Long menuId, long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
            orderLineItem.getId(),
            orderLineItem.getId(),
            orderLineItem.getMenuId(),
            orderLineItem.getQuantity()
        );
    }

    public static List<OrderLineItemResponse> of(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderLineItemResponse::of)
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
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
