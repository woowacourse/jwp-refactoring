package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderFixture {
    public static Order ORDER_ID_STATUS(OrderTable orderTable, OrderStatus orderStatus) {
        return new Order(
                orderTable,
                orderStatus,
                LocalDateTime.now(),
                new ArrayList<>()
        );
    }

    public static Order ORDER(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(
                orderTable,
                null,
                LocalDateTime.now(),
                orderLineItems
        );
    }

    public static Order ORDER(OrderTable orderTable, List<OrderLineItem> orderLineItems, OrderStatus orderStatus) {
        return new Order(
                orderTable,
                orderStatus,
                LocalDateTime.now(),
                orderLineItems
        );
    }

}
