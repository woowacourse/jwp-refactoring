package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderFixture {

    public static Order 주문_생성_메뉴_당_1개씩(final OrderTable orderTable, final List<Menu> menus) {
        final Order order = new Order(orderTable, LocalDateTime.now());
        final List<OrderLineItem> orderLineItems = 주문_항목_목록_생성(order, menus, 1L);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static Order 주문_생성_메뉴_당_1개씩_상태_설정(final OrderTable orderTable,
                                             final OrderStatus orderStatus,
                                             final List<Menu> menus) {
        final Order order = new Order(orderTable, LocalDateTime.now());
        order.setOrderStatus(orderStatus.name());
        final List<OrderLineItem> orderLineItems = 주문_항목_목록_생성(order, menus, 1L);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static List<OrderLineItem> 주문_항목_목록_생성(final Order order,
                                                  final List<Menu> menus,
                                                  final long quantity) {
        return menus.stream()
                .map(menu -> new OrderLineItem(order, menu, quantity))
                .collect(Collectors.toList());
    }
}
