package kitchenpos.application.fixture;

import java.util.ArrayList;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderFixture {

    // 해당 픽스쳐를 이용해 만든 메뉴의 각 상품 개수는 모두 1개로 설정된다.
    private static final int QUANTITY = 1;


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
            orderLineItem.setQuantity(QUANTITY);

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
