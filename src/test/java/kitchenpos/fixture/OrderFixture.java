package kitchenpos.fixture;

import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.table.OrderTable;

public class OrderFixture {

    public static Order 주문(final OrderTable orderTable, final OrderLineItems orderLineItems) {
        return new Order(orderTable, orderLineItems);
    }

    public static OrderLineItem 주문_상품(final Menu menu, final Long quantity) {
        return new OrderLineItem(menu, quantity);
    }

    public static OrderLineItems 주문_상품들(final OrderLineItem... orderLineItems) {
        return new OrderLineItems(Arrays.stream(orderLineItems)
                .collect(Collectors.toUnmodifiableList())
        );
    }
}
