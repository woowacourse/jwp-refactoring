package kitchenpos.fixture;

import domain.Menu;
import domain.Order;
import domain.OrderTable;
import domain.OrderValidator;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import domain.order_lineitem.MenuInfoGenerator;
import domain.order_lineitem.OrderLineItem;
import domain.order_lineitem.OrderLineItems;
import support.AggregateReference;

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
