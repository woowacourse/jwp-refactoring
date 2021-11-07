package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class OrderFixture {

    private static final Long ID = 1L;
    private static final OrderTable ORDER_TABLE = OrderTableFixture.create();
    private static final OrderStatus ORDER_STATUS = OrderStatus.COOKING;
    private static final LocalDateTime ORDERED_TIME = LocalDateTime.now();

    public static Orders create() {
        return create(ID, ORDER_TABLE, ORDER_STATUS, ORDERED_TIME);
    }

    public static Orders create(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        Orders order = new Orders(id, orderTable, orderStatus, orderedTime);

        return order;
    }
}
