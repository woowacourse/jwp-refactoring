package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrderFixture {
    public static final Order FIRST_TABLE_후라이드치킨_하나;
    public static final Order FIRST_TABLE_후라이드치킨_하나_COMPLETION;
    public static final Order FIRST_TABLE_후라이드치킨_하나_NO_KEY;
    public static final Order MEAL_STATUS;
    public static final Order COMPLETION_STATUS;
    public static final LocalDateTime NOW = LocalDateTime.now();

    static {
        FIRST_TABLE_후라이드치킨_하나 = newInstance(1L, 1L, OrderStatus.COOKING.name(), NOW, OrderLineItemFixture.FIRST_FIRST_ORDERLINE);
        FIRST_TABLE_후라이드치킨_하나_COMPLETION = newInstance(1L, 1L, OrderStatus.COMPLETION.name(), NOW, OrderLineItemFixture.FIRST_FIRST_ORDERLINE);
        FIRST_TABLE_후라이드치킨_하나_NO_KEY = newInstance(null, 1L, OrderStatus.COOKING.name(), NOW, OrderLineItemFixture.FIRST_FIRST_ORDERLINE);
        MEAL_STATUS = newInstance(null, null, OrderStatus.MEAL.name(), null, Collections.emptyList());
        COMPLETION_STATUS = newInstance(null, null, OrderStatus.COMPLETION.name(), null, Collections.emptyList());
    }

    public static List<Order> orders() {
        return Collections.singletonList(FIRST_TABLE_후라이드치킨_하나);
    }

    private static Order newInstance(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, OrderLineItem... orderLineItems) {
        return newInstance(id, orderTableId, orderStatus, orderedTime, Arrays.asList(orderLineItems));
    }

    private static Order newInstance(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        final Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);
        return order;
    }
}
