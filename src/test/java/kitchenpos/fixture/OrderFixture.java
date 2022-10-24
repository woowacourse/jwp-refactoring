package kitchenpos.fixture;

import java.util.ArrayList;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderFixture {

    private static final int ONE_QUANTITY = 1;


    public static Order createOrder(final OrderTable orderTable, final Menu... menus) {
        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        setOrderLineItems(order, menus);
        return order;
    }

    private static void setOrderLineItems(final Order order, final Menu[] menus) {
        final ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final Menu menu : menus) {
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(menu.getId());
            orderLineItem.setQuantity(ONE_QUANTITY);
            orderLineItems.add(orderLineItem);
        }
        order.setOrderLineItems(orderLineItems);
    }

    public static Order forUpdateStatus(final OrderStatus orderStatus) {
        final Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        return order;
    }
}
