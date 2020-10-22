package kitchenpos;

import kitchenpos.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.domain.OrderStatus.COOKING;

public class TestFixtureFactory {
    public static Product createProduct(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static MenuGroup createMenuGroup(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    public static MenuProduct createMenuProduct(Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static MenuProduct createMenuProduct(Long productId, long quantity, Long menuId) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        menuProduct.setMenuId(menuId);
        return menuProduct;
    }

    public static Menu createMenu(String name, BigDecimal price, Long menuGroupId, MenuProduct... menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(Arrays.asList(menuProducts));
        return menu;
    }

    public static TableGroup createTableGroup(OrderTable... tables) {
        List<OrderTable> orderTables = Arrays.asList(tables);
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }

    public static OrderTable createEmptyTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        return orderTable;
    }

    public static OrderTable createOccupiedTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        return orderTable;
    }

    public static OrderTable createGroupedTable(Long groupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(groupId);
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(0);
        return orderTable;
    }

    public static OrderTable createTable(boolean emptyStatus, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(emptyStatus);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public static OrderTable createTableToChange(boolean emptyStatus) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(emptyStatus);
        return orderTable;
    }

    public static OrderTable createTableToChange(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public static Order createOrder(Long tableId, OrderLineItem... items) {
        List<OrderLineItem> orderLineItems = Arrays.asList(items);
        Order order = new Order();
        order.setOrderTableId(tableId);
        order.setOrderLineItems(orderLineItems);
        order.setOrderStatus(COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        return order;
    }

    public static Order createOrderToChange(OrderStatus status) {
        Order order = new Order();
        order.setOrderStatus(status.name());
        return order;
    }

    public static OrderLineItem createOrderLineItem(Long menuId, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static OrderLineItem createOrderLineItem(Long menuId, int quantity, Long orderId) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setOrderId(orderId);
        return orderLineItem;
    }
}
