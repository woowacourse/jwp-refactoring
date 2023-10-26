package kitchenpos.domain.order;

import kitchenpos.domain.Money;

public class OrderLineItemFixture {
    public static OrderLineItem id_없는_주문항목() {
        return new OrderLineItem(null, 1L, "메뉴", Money.of(10_000), 1L);
    }

    public static OrderLineItem 주문항목(Long id) {
        return new OrderLineItem(id, 2L, "메뉴", Money.of(10_000), 1L);
    }
}
