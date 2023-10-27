package kitchenpos.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;

import java.util.List;

public class OrderFixture {
    
    public static Order ORDER(OrderTable orderTable, List<OrderLineItem> orderLineItems, OrderStatus orderStatus) {
        return Order.of(
                orderTable,
                orderStatus,
                orderLineItems
        );
    }

}
