package kitchenpos.Fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;

import java.math.BigDecimal;
import java.util.List;

public abstract class Fixture {
    public static Menu menuFixture(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        Menu menu = new Menu(name, price, menuGroup);
        menu.registerMenuProducts(menuProducts);
        return menu;
    }

    public static MenuProduct menuProductFixture(Menu menu, Product product, long quantity) {
        return new MenuProduct(null, menu, product, quantity);
    }

    public static OrderLineItem orderLineItemFixture(Order order, Menu menu, long quantity) {
        return new OrderLineItem(null, order, menu, quantity);
    }
}
