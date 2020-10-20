package kitchenpos.utils;

import kitchenpos.domain.*;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class KitchenPosClassCreator {

    private KitchenPosClassCreator() {
    }

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

    public static MenuProduct createMenuProduct(Product product, long quantity) {
        requireNonNull(product);
        long productId = product.getId();
        requireNonNull(productId);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    public static Menu createMenu(String name, MenuGroup menuGroup, BigDecimal price, List<MenuProduct> menuProducts) {
        requireNonNull(menuGroup);
        long menuGroupId = menuGroup.getId();
        requireNonNull(menuGroupId);

        Menu menu = new Menu();
        menu.setMenuGroupId(menuGroup.getId());
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    public static OrderTable createOrderTable(int numberOfGuest, boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuest);
        orderTable.setEmpty(isEmpty);

        return orderTable;
    }

    public static OrderLineItem createOrderLineItem(Menu menu, long quantity) {
        requireNonNull(menu);
        long menuId = menu.getId();
        requireNonNull(menuId);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }

    public static Order createOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems, String orderStatus) {
        requireNonNull(orderTable);
        long orderTableId = orderTable.getId();
        requireNonNull(orderTableId);

        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        order.setOrderStatus(orderStatus);

        return order;
    }

    public static TableGroup createTableGroup(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        return tableGroup;
    }
}
