package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

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
