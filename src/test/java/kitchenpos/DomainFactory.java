package kitchenpos;

import kitchenpos.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class DomainFactory {
    public static MenuGroup createMenuGroup(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    public static Product createProduct(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static MenuProduct createMenuProduct(Long productId, long quantity) {
        return createMenuProduct(null, productId, quantity);
    }

    public static MenuProduct createMenuProduct(Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static Menu createMenuWithMenuProducts(Long menuGroupId, String name,
                                                  BigDecimal price, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setMenuGroupId(menuGroupId);
        menu.setPrice(price);
        menu.setName(name);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static Menu createMenu(Long menuGroupId, String name, BigDecimal price) {
        Menu menu = new Menu();
        menu.setMenuGroupId(menuGroupId);
        menu.setName(name);
        menu.setPrice(price);
        return menu;
    }

    public static OrderTable createOrderTable(int numberOfGuests, boolean empty) {
        return createOrderTable(numberOfGuests, empty, null);
    }

    public static OrderTable createOrderTable(int numberOfGuests, boolean empty, Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        orderTable.setTableGroupId(tableGroupId);
        return orderTable;
    }

    public static OrderLineItem createOrderLineItem(Long menuId, long quantity) {
        return createOrderLineItem(null, menuId, quantity);
    }

    public static OrderLineItem createOrderLineItem(Long orderId, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static Order createOrder(Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        return order;
    }

    public static TableGroup createTableGroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }
}
