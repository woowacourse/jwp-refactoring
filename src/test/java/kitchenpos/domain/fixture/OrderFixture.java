package kitchenpos.domain.fixture;

import kitchenpos.domain.Orders;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

import static java.time.LocalDateTime.now;

public class OrderFixture {

    public static Orders 주문_생성(OrderTable orderTable) {
        Orders orders = new Orders(orderTable, OrderStatus.COOKING, now());
        return orders;
    }

}