package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
public class OrderLineItemFixture {

    public static OrderLineItem createOrderLineItem(Long seq, long quantity) {
        return OrderLineItem.builder()
                .menu(Menu.builder().build())
                .order(Order.builder().build())
                .quantity(quantity)
                .build();
    }
}
