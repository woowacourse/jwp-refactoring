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
        return Order.of(savedOrderTable.getId(), orderLineItems);
    }

    public static Order 요리중인_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        return Order.of(savedOrderTable.getId(), orderLineItems);
    }

    public static Order 식사중인_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        Order order = Order.of(savedOrderTable.getId(), orderLineItems);
        order.changeOrderStatus(MEAL);

        return order;
    }

    public static Order 식사완료_한_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        Order order = Order.of(savedOrderTable.getId(), orderLineItems);
        order.changeOrderStatus(COMPLETION);

        return order;
    }

    public static Order 존재하지_않는_주문_테이블을_가진_주문_생성(
            List<OrderLineItem> orderLineItems
    ) {
        return Order.of(Long.MAX_VALUE, orderLineItems);
    }

}
