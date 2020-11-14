package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemCreateRequest {
    private final Long menuId;
    private final int quantity;

    public OrderLineItemCreateRequest(Long menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity(Long orderId) {
        return new OrderLineItem(null, null, menuId, quantity);
    }

    public static List<OrderLineItem> listOf(List<OrderLineItemCreateRequest> orderLineItems,
        Long orderId) {
        return orderLineItems.stream()
            .map(item -> item.toEntity(orderId))
            .collect(Collectors.toList());
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }
}
