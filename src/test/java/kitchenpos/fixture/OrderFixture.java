package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

import java.time.LocalDateTime;

import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithId;

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

    public static Order createOrderWithOrderTableAndOrderStatus() {
        return createOrder(null, null, OrderStatus.COOKING);
    }

    public static Order createOrderWithOrderStatus(OrderStatus orderStatus) {
        return createOrder(null, null, orderStatus);
    }

    public static Order createOrderWithOrderTable(OrderTable orderTable) {
        return createOrder(null, orderTable, OrderStatus.MEAL);
    }
}