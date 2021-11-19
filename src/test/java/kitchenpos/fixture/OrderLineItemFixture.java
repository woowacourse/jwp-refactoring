package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Orders;

import java.util.Arrays;
import java.util.List;

public class OrderLineItemFixture {

    private static final Long SEQ = 1L;
    private static final Orders ORDER = OrderFixture.create();
    private static final Menu MENU = MenuFixture.create();
    private static final long QUANTITY = 1L;


    public static List<OrderLineItem> create() {
        OrderLineItem orderLineItem = new OrderLineItem(SEQ, ORDER, MENU, QUANTITY);

        return Arrays.asList(orderLineItem);
    }
}
