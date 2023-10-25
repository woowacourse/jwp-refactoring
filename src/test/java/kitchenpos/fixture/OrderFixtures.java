package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderFixtures {

    public static Order create(
            Long orderTableId,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        return create(orderTableId, null, orderedTime, orderLineItems);
    }

    public static Order create(
            Long orderTableId,
            String orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        return new Order(
                null,
                orderTableId,
                orderStatus,
                orderedTime,
                orderLineItems
        );
    }

}
