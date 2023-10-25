package kitchenpos.fixture;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    public static Order 주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        Order orders = Order.of(OrderStatus.COOKING, orderLineItems);
        savedOrderTable.addOrder(orders);

        return orders;
    }

    public static Order 요리중인_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        Order orders = Order.of(OrderStatus.COOKING, orderLineItems);
        savedOrderTable.addOrder(orders);

        return orders;
    }

    public static Order 식사중인_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        Order orders = Order.of(OrderStatus.MEAL, orderLineItems);
        savedOrderTable.addOrder(orders);

        return orders;
    }

    public static Order 식사완료_한_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        Order orders = Order.of(OrderStatus.COMPLETION, orderLineItems);
        savedOrderTable.addOrder(orders);

        return orders;
    }

    public static Order 존재하지_않는_주문_테이블을_가진_주문_생성(
            List<OrderLineItem> orderLineItems
    ) {
        Order orders = Order.of(OrderStatus.COOKING, orderLineItems);
        OrderTable orderTable = OrderTable.of(0, false);
        orderTable.addOrder(orders);

        return orders;
    }

}
