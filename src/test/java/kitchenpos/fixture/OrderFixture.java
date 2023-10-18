package kitchenpos.fixture;

import java.time.LocalDateTime;
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
        return new Order(
                savedOrderTable.getId(),
                orderLineItems
        );
    }

    public static Order 요리중인_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        return new Order(
                savedOrderTable.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                orderLineItems
        );
    }

    public static Order 식사중인_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        return new Order(
                savedOrderTable.getId(),
                OrderStatus.MEAL.name(),
                LocalDateTime.now(),
                orderLineItems
        );
    }

    public static Order 식사완료_한_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        return new Order(
                savedOrderTable.getId(),
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                orderLineItems
        );
    }

    public static Order 존재하지_않는_OrderTable을_가진_주문_생성(
            List<OrderLineItem> orderLineItems
    ) {
        return new Order(
                Long.MAX_VALUE,
                orderLineItems
        );
    }

}
