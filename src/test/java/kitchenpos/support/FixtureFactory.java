package kitchenpos.support;

import kitchenpos.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FixtureFactory {

    public static Product savedProduct(final Long id, final String name, final BigDecimal price) {
        return new Product(id, name, price.intValue());
    }

    public static MenuGroup forSaveMenuGroup(final String groupName) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(groupName);
        return menuGroup;
    }

    public static MenuGroup savedMenuGroup(final Long id, final String groupName) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(groupName);
        return menuGroup;
    }

    public static Menu forSaveMenu(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static Menu savedMenu(final Long id, final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        final Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static MenuProduct savedMenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static OrderLineItem savedOrderLineItem(final Long id, final Long orderId, final Long menuId, final long quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(id);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static Order forSaveOrder(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static Order savedOrder(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime localDateTime, final List<OrderLineItem> orderLineItems) {
        final Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(localDateTime);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static OrderTable forSaveOrderTable(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable saveOrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static TableGroup forSaveTableGroup(final List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    public static TableGroup savedTableGroup(final Long id, final LocalDateTime localDateTime, final List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(localDateTime);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
