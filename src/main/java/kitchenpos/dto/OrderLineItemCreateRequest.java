package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemCreateRequest {

    private Long menuId;
    private long quantity;

    public OrderLineItemCreateRequest() {
    }

    private OrderLineItemCreateRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemCreateRequest of(OrderLineItem orderLineItem) {
        Menu menu = orderLineItem.getMenu();
        long quantity = orderLineItem.getQuantity();

        return new OrderLineItemCreateRequest(menu.getId(), quantity);
    }

    public static List<OrderLineItemCreateRequest> toRequestList(List<OrderLineItem> menuProducts) {
        return menuProducts.stream()
            .map(OrderLineItemCreateRequest::of)
            .collect(Collectors.toList());
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity(Order savedOrder, Menu menu, long quantity) {
        return new OrderLineItem(savedOrder.getId(), savedOrder, menu, quantity);
    }
}
