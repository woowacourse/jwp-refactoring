package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import kitchenpos.domain.Order;

public class OrderFixture {

    public static Order generateOrder(LocalDateTime orderTime, Long orderTableId, String orderStatus) {
        Order order = new Order();
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderLineItems(new ArrayList<>());
        return order;
    }
}
