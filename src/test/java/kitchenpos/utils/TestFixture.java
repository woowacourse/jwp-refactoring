package kitchenpos.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TestFixture {

    public static MenuProduct getMenuProduct(final Long productId) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(2L);

        return menuProduct;
    }

    public static Menu getMenu(final MenuProduct menuProduct, final Long menuGroupId) {
        final Menu menu = new Menu();
        menu.setName("후라이드+후라이드");
        menu.setPrice(BigDecimal.valueOf(16000));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        return menu;
    }

    public static MenuGroup getMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroup;
    }

    public static OrderTable getOrderTableWithEmpty() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        return orderTable;
    }

    public static OrderTable getOrderTableWithNotEmpty() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(false);

        return orderTable;
    }

    public static TableGroup getTableGroup(final OrderTable orderTable1, final OrderTable orderTable2) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        return tableGroup;
    }

    public static OrderLineItem getOrderLineItem() {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);

        return orderLineItem;
    }

    public static Order getOrderWithCooking(final OrderLineItem orderLineItem, final Long orderTableId) {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        return order;
    }

    public static Order getOrderWithCompletion(final OrderLineItem orderLineItem) {
        final Order order = new Order();
        order.setOrderTableId(7L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());

        return order;
    }
}
