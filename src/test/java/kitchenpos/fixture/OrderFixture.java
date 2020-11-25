package kitchenpos.fixture;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;

import java.time.LocalDateTime;

import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithId;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithoutId;

public class OrderFixture {

    public static Order createOrder(Long id, OrderTable orderTable, OrderStatus orderStatus) {
        return new Order(id, orderTable, orderStatus, LocalDateTime.now());
    }

    public static Order createOrderWithOrderTableAndOrderStatus(OrderTable orderTable, OrderStatus orderStatus) {
        return createOrder(null, orderTable, orderStatus);
    }

    public static Order createOrderWithId(Long id) {
        return createOrder(id, createOrderTableWithId(1L), OrderStatus.COOKING);
    }

    public static Order createOrderWithOrderStatus(OrderStatus orderStatus) {
        return createOrder(null, createOrderTableWithoutId(), orderStatus);
    }

    public static Order createOrderWithOrderTable(OrderTable orderTable) {
        return createOrder(null, orderTable, OrderStatus.MEAL);
    }

}