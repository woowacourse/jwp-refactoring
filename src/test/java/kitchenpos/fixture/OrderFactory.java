package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderFactory {

    public static Order order(final OrderTable table, final Menu... menus) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (Menu menu : menus) {
            final var orderLineItem = new OrderLineItem(null, null, menu.getId(), 1);
            orderLineItems.add(orderLineItem);
        }

        return new Order(null, table.getId(), null, null, orderLineItems);
    }

    public static Order order(final OrderTable table, final OrderStatus status, final Menu... menus) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (Menu menu : menus) {
            final var orderLineItem = new OrderLineItem(null, null, menu.getId(), 1);
            orderLineItems.add(orderLineItem);
        }

        return new Order(null, table.getId(), status, LocalDateTime.now(), orderLineItems);
    }
}
