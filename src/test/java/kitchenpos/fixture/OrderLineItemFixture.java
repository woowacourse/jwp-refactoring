package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

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
