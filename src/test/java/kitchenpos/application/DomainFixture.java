package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

public class DomainFixture {

    public static Menu getMenu(final Long menuGroupId, final List<MenuProduct> menuProducts) {
        final Menu menu = new Menu(null, "마이쮸 포도맛", BigDecimal.valueOf(800), menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static Product getProduct() {
        return new Product("마이쮸", BigDecimal.valueOf(800));
    }

    public static OrderTable getEmptyTable() {
        return new OrderTable(0, true);
    }

    public static OrderTable getNotEmptyTable(final int numberOfGuests) {
        return new OrderTable(numberOfGuests, false);
    }

    public static MenuGroup getMenuGroup() {
        return new MenuGroup("마이쮸 1종 세트");
    }

    public static Order getOrder(final Long orderTableId, final Long menuId) {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(getOrderLineItems(menuId, 1));
        return order;
    }

    private static List<OrderLineItem> getOrderLineItems(final Long menuId, final long quantity) {
        return List.of(new OrderLineItem(null, null, menuId, quantity));
    }
}
