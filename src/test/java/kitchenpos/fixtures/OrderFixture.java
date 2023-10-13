package kitchenpos.fixtures;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

public class OrderFixture {

    public static Order ORDER(final List<OrderLineItem> orderLineItems,
                              final OrderTable orderTable) {
        final Order order = new Order();
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTable.getId());
        return order;
    }
}
