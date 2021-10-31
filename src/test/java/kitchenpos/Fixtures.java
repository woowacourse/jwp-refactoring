package kitchenpos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable.BinaryOp.Or;

public class Fixtures {

    public static MenuGroup makeMenuGroup() {
        MenuGroup menuGroup = new MenuGroup(1L, "한마리치킨");
        return menuGroup;
    }

    public static MenuProduct makeMenuProduct() {
        MenuProduct menuProduct = new MenuProduct();


        return menuProduct;
    }

    public static Product makeProduct() {
        Product product = new Product();

        return product;
    }

    public static Menu makeMenu() {
        MenuGroup menuGroup = makeMenuGroup();
        return new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000.00), menuGroup);
    }

    public static OrderLineItem makeOrderLineItem() {
        OrderLineItem orderLineItem = new OrderLineItem();


        return orderLineItem;
    }

    public static Order makeOrder() {
        OrderTable orderTable = makeOrderTable();
        Order order = new Order(1L, orderTable, OrderStatus.COOKING);

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
