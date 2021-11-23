package kitchenpos.application.common.factory;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderFactory {

    private OrderFactory() {

    }

    public static Order create(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(orderLineItems);
        return order;
    }
}
