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
        return new Orders(
                savedOrderTable.getId(),
                orderLineItems
        );
    }

    public static Orders 요리중인_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        return new Orders(
                savedOrderTable.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                orderLineItems
        );
    }

    public static Orders 식사중인_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        return new Orders(
                savedOrderTable.getId(),
                OrderStatus.MEAL.name(),
                LocalDateTime.now(),
                orderLineItems
        );
    }

    public static Orders 식사완료_한_주문_생성(
            OrderTable savedOrderTable,
            List<OrderLineItem> orderLineItems
    ) {
        return new Orders(
                savedOrderTable.getId(),
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                orderLineItems
        );
    }

    public static Orders 존재하지_않는_주문_테이블을_가진_주문_생성(
            List<OrderLineItem> orderLineItems
    ) {
        return new Orders(
                Long.MAX_VALUE,
                orderLineItems
        );
    }

}
