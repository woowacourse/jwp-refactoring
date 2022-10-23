package kitchenpos.application;

import static kitchenpos.application.MenuFixture.MENU_FIRST_ID;
import static kitchenpos.application.MenuFixture.MENU_SECOND_ID;
import static kitchenpos.application.MenuFixture.MENU_THIRD_ID;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

public class OrderFixture {

    public static final Long ORDER_TABLE_NOT_EMPTY_ID = 1L;
    public static final Long ORDER_TABLE_EMPTY_ID = 2L;
    public static final List<OrderLineItem> ORDER_LINE_ITEMS = List.of(
            new OrderLineItem(MENU_FIRST_ID, 3),
            new OrderLineItem(MENU_SECOND_ID, 2),
            new OrderLineItem(MENU_THIRD_ID, 1)
    );
    public static final Order UNSAVED_ORDER = new Order(ORDER_TABLE_NOT_EMPTY_ID, ORDER_LINE_ITEMS);
    public static final OrderTable ORDER_TABLE = new OrderTable(10, true);
}
