package kitchenpos;

import kitchenpos.domain.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

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
        return new MenuProduct(menu,product, quantity);
    }

    public static MenuProduct 새로운_메뉴_상품2(final Menu menu,
                                         final Product product,
                                         final long quantity) {
        return new MenuProduct(menu,product, quantity);
    }

    public static Menu 새로운_메뉴(final String name,
                              final BigDecimal price,
                              final MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }

    public static Order 새로운_주문(final OrderTable orderTable,
                               final List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderLineItems);
    }

    public static OrderLineItem 새로운_주문_항목(final Menu menu,
                                          final long quantity) {
        return new OrderLineItem(menu, quantity);
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
