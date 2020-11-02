package kitchenpos.fixture;

import java.util.Collections;
import java.util.List;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {
    public static final OrderLineItem ORDER_LINE_ITEM1 = TestObjectUtils.createOrderLineItem(
            1L, 1L, 1L, 1
    );

    public static final OrderLineItem ORDER_LINE_ITEM2 = TestObjectUtils.createOrderLineItem(
            2L, 2L, 1L, 1
    );

    public static final OrderLineItem ORDER_LINE_ITEM3 = TestObjectUtils.createOrderLineItem(
            3L, 3L, 1L, 1
    );

    public static final List<OrderLineItem> ORDER_LINE_ITEMS1 = Collections.singletonList(
            ORDER_LINE_ITEM1);

    public static final List<OrderLineItem> ORDER_LINE_ITEMS3 = Collections.singletonList(
            ORDER_LINE_ITEM3);

}
