package kitchenpos.domain;

import kitchenpos.Money;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {
    public static OrderLineItem id_없는_주문항목() {
        return OrderLineItem.of(null, 1L, "메뉴", Money.of(10_000), 1L);
    }

    public static OrderLineItem 주문항목(Long id) {
        return OrderLineItem.of(id, 2L, "메뉴", Money.of(10_000), 1L);
    }
}
