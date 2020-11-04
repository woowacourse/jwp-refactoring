package kitchenpos.fixture;

import java.util.List;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {
    public static final Long ID1 = 1L;
    public static final Long ID2 = 2L;
    public static final String STATUS1 = OrderStatus.MEAL.name();
    public static final String STATUS2 = OrderStatus.COOKING.name();

    public static Order createWithoutId(String status, Long tableId, List<OrderLineItem> items) {
        Order order = new Order();
        order.setOrderStatus(status);
        order.setOrderTableId(tableId);
        order.setOrderLineItems(items);

        return order;
    }

    public static Order createWithId(Long id, String status, Long tableId, List<OrderLineItem> items) {
        Order order = new Order();
        order.setId(id);
        order.setOrderStatus(status);
        order.setOrderTableId(tableId);
        order.setOrderLineItems(items);

        return order;
    }
}
