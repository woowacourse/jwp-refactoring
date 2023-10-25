package kitchenpos.support;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.menu.domain.Product;
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
        return new Menu(name, price, menuGroup);
    }

    public static MenuProduct 새로운_메뉴_상품(Menu menu, Product product, long quantity) {
        return new MenuProduct(menu, product, quantity);
    }

    public static Order 새로운_주문(
            OrderTable orderTable,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        return new Order(null, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public static OrderLineItem 새로운_주문_항목(Menu menu, long quantity) {
        return new OrderLineItem(menu, quantity);
    }

    public static OrderTable 새로운_주문_테이블(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(null, tableGroup, numberOfGuests, empty);
    }

    public static TableGroup 새로운_단체_지정(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }
}
