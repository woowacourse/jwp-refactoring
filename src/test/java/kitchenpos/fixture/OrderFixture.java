package kitchenpos.fixture;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.vo.OrderStatus;

public class OrderFixture {

    public static Order 주문_생성_메뉴_당_1개씩(final List<Menu> menus) {
        final Order order = new Order(1L, 주문_항목_목록_생성(menus, 1L));
        return order;
    }

    public static Order 주문_생성_메뉴_당_1개씩_상태_설정(final OrderStatus orderStatus, final List<Menu> menus) {
        final Order order = new Order(1L, 주문_항목_목록_생성(menus, 1L));
        order.changeOrderStatus(orderStatus);
        return order;
    }

    public static List<OrderLineItem> 주문_항목_목록_생성(final List<Menu> menus, final long quantity) {
        return menus.stream()
                .map(menu -> new OrderLineItem(menu, quantity))
                .collect(Collectors.toList());
    }
}
