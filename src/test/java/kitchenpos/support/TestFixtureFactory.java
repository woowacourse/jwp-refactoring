package kitchenpos.support;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
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
