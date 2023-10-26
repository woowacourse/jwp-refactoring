package kitchenpos.support;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTableLog;
import kitchenpos.order.domain.OrderedMenu;
import kitchenpos.order.domain.OrderedMenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.springframework.stereotype.Component;

@Component
public class TestFixtureFactory {

    private TestFixtureFactory() {
    }

    public static Product 새로운_상품(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

    public static MenuGroup 새로운_메뉴_그룹(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static Menu 새로운_메뉴(
            String name,
            BigDecimal price,
            MenuGroup menuGroup
    ) {
        return new Menu(name, price, menuGroup.getId());
    }

    public static MenuProduct 새로운_메뉴_상품(Menu menu, Product product, long quantity) {
        return new MenuProduct(menu, product.getName(), product.getPrice(), quantity);
    }

    public static Order 새로운_주문(
            OrderStatus orderStatus,
            LocalDateTime orderedTime
    ) {
        return new Order(null, orderStatus, orderedTime);
    }

    public static OrderLineItem 새로운_주문_항목(Order order, OrderedMenu orderedMenu, long quantity) {
        return new OrderLineItem(order, orderedMenu, quantity);
    }

    public static OrderTable 새로운_주문_테이블(Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(null, tableGroupId, numberOfGuests, empty);
    }

    public static TableGroup 새로운_단체_지정() {
        return new TableGroup();
    }

    public static OrderedMenu 새로운_주문_메뉴(String name, BigDecimal price, MenuGroup menuGroup) {
        return new OrderedMenu(name, new Price(price), menuGroup.getName());
    }

    public static OrderedMenuProduct 새로운_주문_메뉴_상품(OrderedMenu orderedMenu, MenuProduct menuProduct, long quantity) {
        return new OrderedMenuProduct(orderedMenu, menuProduct.getName(), menuProduct.getPrice(), quantity);
    }

    public static OrderTableLog 새로운_주문_테이블_로그(Order order, Long orderTableId, int numberOfGuests) {
        return new OrderTableLog(order, orderTableId, numberOfGuests);
    }
}
