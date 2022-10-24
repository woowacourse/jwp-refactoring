package kitchenpos.ui;

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

public class DomainFixture {

    public static Product getProduct() {
        final Product product = new Product();
        product.setId(1L);
        product.setName("productName");
        product.setPrice(BigDecimal.valueOf(1000L));
        return product;
    }

    public static MenuGroup getMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("menuGroup");
        return menuGroup;
    }

    public static Menu getMenu() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setId(1L);
        menuProduct.setQuantity(2);
        menuProduct.setProductId(1L);
        menuProduct.setMenuId(1L);
        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("menu");
        menu.setPrice(BigDecimal.valueOf(1000L));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(List.of(menuProduct));
        return menu;
    }

    public static OrderTable getOrderTable(final boolean isEmpty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }

    public static Order getOrder() {
        final Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(1L);
        orderLineItem.setId(1L);
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(List.of(orderLineItem));
        return order;
    }

    public static TableGroup getTableGroup() {
        final OrderTable orderTable1 = getOrderTable(false);
        orderTable1.setTableGroupId(1L);
        final OrderTable orderTable2 = getOrderTable(false);
        orderTable2.setId(2L);
        orderTable2.setTableGroupId(1L);
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));
        tableGroup.setId(1L);
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }
}
