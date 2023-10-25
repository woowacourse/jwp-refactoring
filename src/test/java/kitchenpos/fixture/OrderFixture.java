package kitchenpos.fixture;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    public static Order 주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        Order order = Order.of(orderLineItems);
        savedOrderTable.addOrder(order);

        return order;
    }

    public static Order 요리중인_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        Order order = Order.of(orderLineItems);
        savedOrderTable.addOrder(order);

        return order;
    }

    public static Order 식사중인_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        Order order = Order.of(orderLineItems);
        order.changeOrderStatus(MEAL);
        savedOrderTable.addOrder(order);

        return order;
    }

    public static Order 식사완료_한_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        Order order = Order.of(orderLineItems);
        order.changeOrderStatus(COMPLETION);
        savedOrderTable.addOrder(order);

        return order;
    }

    public static Order 존재하지_않는_주문_테이블을_가진_주문_생성(
            List<OrderLineItem> orderLineItems
    ) {
        Order order = Order.of(orderLineItems);
        OrderTable orderTable = OrderTable.of(0, false);
        orderTable.addOrder(order);

        return order;
    }

}
