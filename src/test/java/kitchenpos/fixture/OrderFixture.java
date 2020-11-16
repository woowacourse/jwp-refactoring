package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

import java.time.LocalDateTime;

public class OrderFixture {

    public static Order createOrder(Long id, OrderTable orderTable, OrderStatus orderStatus) {
        return new Order(id, orderTable, orderStatus, LocalDateTime.now());
    }

    public static Order createOrderWithoutId(OrderTable orderTable, OrderStatus orderStatus) {
        return createOrder(null, orderTable, orderStatus);
    }

    /*
    public static Order createOrderWithId(Long id) {
        return createOrder(id, );
    }

    public static Order createOrderWithoutId(Long tableId, String orderStatus, OrderLineItem orderLineItem) {
        return createOrder(null, orderStatus, tableId, Arrays.asList(orderLineItem));
    }

    public static Order createOrderWithOrderStatus(String orderStatus) {
        return createOrder(null, orderStatus, null, null);
    }

    public static Order createOrderWithOrderStatusAndTableId(String orderStatus, Long tableId) {
        return createOrder(null, orderStatus, tableId, null);
    }

    public static Order createOrderEmptyOrderLineItem() {
        return createOrder(null, null, null, null);
    }

     */
}
