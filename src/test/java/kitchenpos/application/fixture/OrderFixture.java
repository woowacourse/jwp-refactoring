package kitchenpos.application.fixture;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    public static Order createOrder(final Long id,
                                    final Long orderTableId,
                                   final List<OrderLineItem> lineItems) {
        return new Order(id, orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now(), lineItems);
    }

    public static Order createOrder(final Long id, final Long orderTableId) {
        return createOrder(id, orderTableId, Collections.emptyList());
    }
}
