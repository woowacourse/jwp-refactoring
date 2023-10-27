package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderValidator;
import kitchenpos.domain.order.order_lineitem.MenuInfoGenerator;
import kitchenpos.domain.order.order_lineitem.OrderLineItem;
import kitchenpos.domain.order.order_lineitem.OrderLineItems;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.support.AggregateReference;
import org.aspectj.weaver.ast.Or;

public class OrderFixture {

    public static Order 주문(
            final OrderTable orderTable,
            final OrderLineItems orderLineItems,
            final OrderValidator orderValidator,
            final LocalDateTime localDateTime
    ) {
        return new Order(new AggregateReference<>(orderTable.getId()), orderLineItems, orderValidator, localDateTime);
    }

    public static OrderLineItem 주문_상품(final Menu menu, final Long quantity, final MenuInfoGenerator menuInfoGenerator) {
        return new OrderLineItem(new AggregateReference<>(menu.getId()), quantity, menuInfoGenerator);
    }

    public static OrderLineItems 주문_상품들(final OrderLineItem... orderLineItems) {
        return new OrderLineItems(Arrays.stream(orderLineItems)
                .collect(Collectors.toUnmodifiableList())
        );
    }
}
