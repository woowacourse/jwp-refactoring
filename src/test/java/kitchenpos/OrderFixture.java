package kitchenpos;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.domain.order.OrderStatus.MEAL;

public class OrderFixture {

    private static final OrderStatus ORDER_STATUS = MEAL;
    private static final LocalDateTime ORDER_TIME = LocalDateTime.now();
    private static final long ORDER_LINE_QUANTITY = 1;

    public static Order createOrder(Long id) {
        return new Order(ORDER_STATUS, Arrays.asList(createOrderLineItem(null)));
    }

    public static OrderLineItem createOrderLineItem(Menu menu) {
        return new OrderLineItem(menu, ORDER_LINE_QUANTITY);
    }
}
