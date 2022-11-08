package kitchenpos.fixture.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

public class OrderFixture {

    // 해당 픽스쳐를 이용해 만든 메뉴의 각 상품 개수는 모두 1개로 설정된다.
    private static final int QUANTITY = 1;

    public static Order createOrder(final OrderTable orderTable, final OrderStatus orderStatus, final Menu... menus) {
        return new Order(orderTable.getId(), orderStatus, LocalDateTime.now(), createOrderLineItems(menus));
    }

    private static List<OrderLineItem> createOrderLineItems(final Menu[] menus) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final Menu menu : menus) {
            orderLineItems.add(new OrderLineItem(menu.getId(), menu.getName(), menu.getPrice(), QUANTITY));
        }
        return orderLineItems;
    }
}
