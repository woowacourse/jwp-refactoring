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
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("후라이드치킨");

        return menuGroup;
    }

    public static MenuProduct makeMenuProduct() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setMenuId(1L);
        menuProduct.setQuantity(1);

        return menuProduct;
    }

    public static Product makeProduct() {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(16000.00));
        product.setId(1L);
        product.setName("후라이드치킨");
        return product;
    }

    public static Menu makeMenu() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setPrice(BigDecimal.valueOf(16000.00));
        menu.setName("후라이드치킨");
        menu.setMenuGroupId(1L);

        return menu;
    }

    public static OrderLineItem makeOrderLineItem() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);

        return orderLineItem;
    }

    public static Order makeOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(1L);
        return order;
    }

    public static OrderTable makeOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(1);
        return orderTable;
    }

    public static TableGroup makeTableGroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        return tableGroup;
    }
}
