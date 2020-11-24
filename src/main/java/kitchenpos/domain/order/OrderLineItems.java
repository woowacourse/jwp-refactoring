package kitchenpos.domain.order;

import kitchenpos.domain.menu.Menu;
import kitchenpos.dto.order.response.OrderResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderLineItems {
    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = new ArrayList<>(orderLineItems);
    }

    public static OrderLineItems of(Order order, List<Menu> menus, Map<Long, Long> menuIdToQuantity) {
        List<OrderLineItem> orderLineItems = menus.stream()
                .map(menu -> new OrderLineItem(order, menu, menuIdToQuantity.get(menu.getId())))
                .collect(Collectors.toList());
        return new OrderLineItems(orderLineItems);
    }

    public List<OrderResponse> createOrderResponses() {
        Set<Order> orders = extractDistinctOrder(orderLineItems);
        return orders.stream()
                .map(order -> OrderResponse.of(order, selectiveByOrder(order, orderLineItems)))
                .collect(Collectors.toList());
    }

    private Set<Order> extractDistinctOrder(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getOrder)
                .collect(Collectors.toSet());
    }

    private List<OrderLineItem> selectiveByOrder(Order order, List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .filter(orderLineItem -> orderLineItem.equalsByOrderId(order.getId()))
                .collect(Collectors.toList());
    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
