package kitchenpos.domain.fixture;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;

import static java.time.LocalDateTime.now;
import static kitchenpos.domain.OrderStatus.*;

public class OrderFixture {

    public static Orders 주문_생성(OrderTable orderTable) {
        Orders orders = new Orders(orderTable, COOKING, now());
        return orders;
    }

}