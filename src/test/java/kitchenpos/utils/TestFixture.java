package kitchenpos.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

public class TestFixture {

    public static Product getProduct(final int price) {
        return new Product("후라이드 치킨", BigDecimal.valueOf(price));
    }

    public static MenuProduct getMenuProduct(final Long productId) {
        return new MenuProduct(productId, 2L);
    }

    public static Menu getMenu(final MenuProduct menuProduct, final Long menuGroupId, final int price) {

        return new Menu("후라이드+후라이드", BigDecimal.valueOf(price), menuGroupId, Collections.singletonList(menuProduct));
    }

    public static MenuGroup getMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup(name);

        return menuGroup;
    }

    public static OrderTable getOrderTableWithEmpty() {
        final OrderTable orderTable = new OrderTable(0, true);

        return orderTable;
    }

    public static OrderTable getOrderTableWithNotEmpty() {
        final OrderTable orderTable = new OrderTable(0, false);

        return orderTable;
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
