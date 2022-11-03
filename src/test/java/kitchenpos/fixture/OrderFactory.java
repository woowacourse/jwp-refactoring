package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;

public class OrderFactory {

    public static Order order(final OrderTable table, final Menu... menus) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (Menu menu : menus) {
            final var orderLineItem = new OrderLineItem(null, null, menu.getId(), 1);
            orderLineItems.add(orderLineItem);
        }

        return new Order(null, table, table.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public static Order order(final OrderTable table, final OrderStatus status, final Menu... menus) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (Menu menu : menus) {
            final var orderLineItem = new OrderLineItem(null, null, menu.getId(), 1);
            orderLineItems.add(orderLineItem);
        }

        return new Order(null, table, table.getId(), status, LocalDateTime.now(), orderLineItems);
    }
}
