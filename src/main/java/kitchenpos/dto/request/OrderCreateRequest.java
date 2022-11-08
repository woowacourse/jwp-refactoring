package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemCreateRequest> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> toMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public Order toEntity(List<Menu> menus) {
        return Order.newCookingInstanceOf(orderTableId, toOrderLineItemEntity(menus));
    }

    private List<OrderLineItem> toOrderLineItemEntity(List<Menu> menus) {
        return orderLineItems.stream()
                .map(orderLineItem -> toOrderLineItem(menus, orderLineItem))
                .collect(Collectors.toList());
    }

    private OrderLineItem toOrderLineItem(List<Menu> menus, OrderLineItemCreateRequest orderLineItem) {
        Menu findMenu = menus.stream()
                .filter(menu -> menu.getId().equals(orderLineItem.getMenuId()))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);

        return new OrderLineItem(null, findMenu.getName(), findMenu.getPrice(), orderLineItem.getQuantity());
    }
}
