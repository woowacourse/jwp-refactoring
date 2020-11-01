package kitchenpos.fixture;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {
    public static final OrderLineItem ORDER_LINE_ITEM1 = TestObjectUtils.createOrderLineItem(
            1L, 1L, 1L, 1
    );
}
