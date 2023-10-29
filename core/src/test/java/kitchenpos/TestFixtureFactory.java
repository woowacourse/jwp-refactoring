package kitchenpos;

import kitchenpos.Product.domain.Product;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TestFixtureFactory {

    private TestFixtureFactory() {
    }

    public static Product 새로운_상품(final Long id,
                                 final String name,
                                 final BigDecimal price) {
        return new Product(id, name, price);
    }

    public static MenuGroup 새로운_메뉴_그룹(final String name) {
        return new MenuGroup(name);
    }

    public static MenuProduct 새로운_메뉴_상품(final Menu menu,
                                        final Product product,
                                        final long quantity) {
        return new MenuProduct(menu, product, quantity);
    }

    public static MenuProduct 새로운_메뉴_상품2(final Menu menu,
                                         final Product product,
                                         final long quantity) {
        return new MenuProduct(menu, product, quantity);
    }

    public static Menu 새로운_메뉴(final String name,
                              final BigDecimal price,
                              final MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }

    public static Order 새로운_주문(final OrderTable orderTable) {
        return new Order(orderTable);
    }

    public static OrderLineItem 새로운_주문_항목(final Order order,
                                          final Menu menu,
                                          final long quantity) {
        return new OrderLineItem(order, menu, quantity);
    }

    public static OrderTable 새로운_주문_테이블(final TableGroup tableGroup,
                                        final int numberOfGuests,
                                        final boolean empty) {
        return new OrderTable(null, tableGroup, numberOfGuests, empty);
    }

    public static TableGroup 새로운_단체_지정() {
        return new TableGroup();
    }

}
