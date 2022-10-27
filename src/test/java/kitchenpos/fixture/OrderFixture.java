package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderFixture {

    public static Order generateOrder(LocalDateTime orderTime, Long orderTableId, String orderStatus, ArrayList<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderedTime(orderTime);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderLineItems(orderLineItems);
        return order;
    }
}
