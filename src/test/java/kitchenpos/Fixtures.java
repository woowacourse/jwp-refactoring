package kitchenpos;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.TableGroup;

public class Fixtures {

    public static MenuGroup makeMenuGroup() {
        return new MenuGroup(1L, "한마리치킨");
    }

    public static MenuProduct makeMenuProduct() {
        MenuProduct menuProduct = new MenuProduct();

        return menuProduct;
    }

    public static Product makeProduct() {
        return new Product(1L, "후라이드", BigDecimal.valueOf(16000.00));
    }

    public static Menu makeMenu() {
        MenuGroup menuGroup = makeMenuGroup();
        return new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000.00), menuGroup.getId());
    }

    public static OrderLineItem makeOrderLineItem() {
        OrderLineItem orderLineItem = new OrderLineItem();

        return orderLineItem;
    }

    public static Order makeOrder() {
        OrderTable orderTable = makeOrderTable();
        Order order = new Order(1L, orderTable.getId(), OrderStatus.COOKING);

        return order;
    }

    public static OrderTable makeOrderTable() {
        OrderTable orderTable = new OrderTable(1L, null, 1, true);

        return orderTable;
    }

    public static TableGroup makeTableGroup() {
        return new TableGroup(1L);
    }
}
