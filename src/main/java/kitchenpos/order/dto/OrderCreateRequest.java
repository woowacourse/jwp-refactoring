package kitchenpos.order.dto;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.exception.NotFoundMenuException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toOrder(final List<Menu> menus) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : this.orderLineItems) {
            final OrderMenu orderMenu = getOrderMenu(menus, orderLineItemRequest);
            final OrderLineItem orderLineItem = orderLineItemRequest.toOrderLineItem(orderMenu);
            orderLineItems.add(orderLineItem);
        }
        return new Order(null, orderTableId, null, null, orderLineItems);
    }

    private static OrderMenu getOrderMenu(final List<Menu> menus, final OrderLineItemRequest orderLineItemRequest) {
        final Long menuId = orderLineItemRequest.getMenuId();
        final Menu menu = menus.stream()
                .filter(it -> it.getId().equals(menuId))
                .findFirst()
                .orElseThrow(NotFoundMenuException::new);
        return new OrderMenu(null, menu.getName(), menu.getPrice());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
