package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Orders;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    public static Orders 주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        Orders orders = Orders.of(OrderStatus.COOKING, orderLineItems);
        savedOrderTable.addOrder(orders);

        return orders;
    }

    public static Orders 요리중인_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        Orders orders = Orders.of(OrderStatus.COOKING, orderLineItems);
        savedOrderTable.addOrder(orders);

        return orders;
    }

    public static Orders 식사중인_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        Orders orders = Orders.of(OrderStatus.MEAL, orderLineItems);
        savedOrderTable.addOrder(orders);

        return orders;
    }

    public static Orders 식사완료_한_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        Orders orders = Orders.of(OrderStatus.COMPLETION, orderLineItems);
        savedOrderTable.addOrder(orders);

        return orders;
    }

    public static Orders 존재하지_않는_주문_테이블을_가진_주문_생성(
            List<OrderLineItem> orderLineItems
    ) {
        Orders orders = Orders.of(OrderStatus.COOKING, orderLineItems);
        OrderTable orderTable = OrderTable.of(0, false);
        orderTable.addOrder(orders);

        return orders;
    }

}
