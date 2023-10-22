package kitchenpos.fixture;

import static kitchenpos.fixture.ProductFixture.치킨_8000원;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.OrderStatus;

public class OrderFixture {

    public static Order 주문_생성_메뉴_당_1개씩(final OrderTable orderTable, final List<Menu> menus) {
        final Order order = new Order(orderTable, 주문_항목_목록_생성(menus, 1L));
        return order;
    }

    public static Order 주문_생성_메뉴_당_1개씩_상태_설정(final OrderTable orderTable,
                                             final OrderStatus orderStatus,
                                             final List<Menu> menus) {
        final Order order = new Order(orderTable, 주문_항목_목록_생성(menus, 1L));
        order.changeOrderStatus(orderStatus);
        return order;
    }

    public static List<OrderLineItem> 주문_항목_목록_생성(final List<Menu> menus,
                                                  final long quantity) {
        return menus.stream()
                .map(menu -> new OrderLineItem(menu, quantity))
                .collect(Collectors.toList());
    }

    public static List<OrderLineItem> 주문항목_1개_메뉴_1000원_할인_치킨() {
        final BigDecimal price = BigDecimal.valueOf(7000);
        final MenuGroup menuGroup = new MenuGroup("양식");
        final Product product = 치킨_8000원();
        final Menu menu = new Menu("1000원 할인 치킨", price, menuGroup, List.of(new MenuProduct(product, 1L)));
        return List.of(new OrderLineItem(menu, 1L));
    }
}
