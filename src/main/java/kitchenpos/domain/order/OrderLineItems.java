package kitchenpos.domain.order;

import kitchenpos.domain.menu.Menu;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItems {
    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems, Order order) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
        this.orderLineItems = orderLineItems;
        validateNotDuplicateMenu();
        updateOrder(order);
    }

    private void validateNotDuplicateMenu() {
        final List<Menu> menus = orderLineItems.stream()
                .map(OrderLineItem::getMenu)
                .collect(Collectors.toList());

        if (orderLineItems.size() != new LinkedHashSet<>(menus).size()) {
            throw new IllegalArgumentException();
        }
    }

    public void updateOrder(Order order) {
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.updateOrder(order);
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
