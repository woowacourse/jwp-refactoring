package kitchenpos.domain.fixture;

import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderMenu;

@SuppressWarnings("NonAsciiCharacters")
public class OrderLineItemFixture {

    private Long seq;
    private OrderMenu orderMenu;
    private long quantity;

    private OrderLineItemFixture() {
    }

    public static OrderLineItem 주문_항목_1번(final OrderMenu orderMenu) {
        return 주문_항목()
            .주문한_메뉴(orderMenu)
            .build();
    }

    private static OrderLineItemFixture 주문_항목() {
        return new OrderLineItemFixture();
    }

    private OrderLineItemFixture 주문한_메뉴(final OrderMenu orderMenu) {
        this.orderMenu = orderMenu;
        return this;
    }

    private OrderLineItem build() {
        return new OrderLineItem(seq, orderMenu, quantity);
    }
}
