package kitchenpos.fixture;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderFixture {

    public static Order 주문_생성(
            final OrderTable orderTable,
            final OrderStatus orderStatus,
            final List<Menu> menus
    ) {
        final Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderTableId(orderTable.getId());
        final List<OrderLineItem> orderLineItems = 주문_항목_목록_생성(menus);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static Order 주문_생성(
            final OrderTable orderTable,
            final List<Menu> menus
    ) {
        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        final List<OrderLineItem> orderLineItems = 주문_항목_목록_생성(menus);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static List<OrderLineItem> 주문_항목_목록_생성(final List<Menu> menus) {
        return menus.stream()
                .map(menu -> {
                    final OrderLineItem orderLineItem = new OrderLineItem();
                    orderLineItem.setMenuId(menu.getId());
                    return orderLineItem;
                }).collect(Collectors.toList());
    }
}
