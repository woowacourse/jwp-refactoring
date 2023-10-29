package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.OrderTable;

public class OrderFixture {

    public static Order 주문(final Long id, final OrderTable orderTable, final OrderStatus orderStatus,
                           final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable.getId(), orderStatus, orderLineItems);
    }
}
