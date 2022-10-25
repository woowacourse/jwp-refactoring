package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderFixture {

    public static Order create(final OrderTable orderTable, final OrderStatus orderStatus, final OrderLineItem... orderLineItems) {
        final Order order = new Order();
        order.setOrderedTime(LocalDateTime.now());
        if (orderTable != null) {
            order.setOrderTableId(orderTable.getId());
        }
        if (orderStatus != null) {
            order.setOrderStatus(orderStatus.name());
        }
        order.setOrderLineItems(Arrays.asList(orderLineItems));
        return order;
    }
}
