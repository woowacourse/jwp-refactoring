package kitchenpos;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Price;

import java.util.List;

public class OrderFixtures {

    private OrderFixtures() {
    }

    public static Order create(final Long orderTableId, final Long menuId) {
        final OrderLineItem orderLineItem = new OrderLineItem(menuId, "연어", Price.valueOf(3000), 2);

        return new Order(orderTableId, List.of(orderLineItem));
    }

    public static Order createWithStatusCooking(final Long orderTableId, final Long menuId) {
        final OrderLineItem orderLineItem = new OrderLineItem(menuId, "연어", Price.valueOf(3000), 2);

        return new Order(null, orderTableId, OrderStatus.COOKING, null, List.of(orderLineItem));
    }
}
