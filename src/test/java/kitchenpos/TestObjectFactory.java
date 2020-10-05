package kitchenpos;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TestObjectFactory {
    public static OrderTable creatOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);
        return orderTable;
    }

    public static OrderTable createChangeEmptyOrderTableDto(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable createChangeNumberOfGuestsDto(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public static Order createOrder(Long orderTableId, String orderStatus, List<OrderLineItem> items) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(items);
        return order;
    }

    public static Order createOrderRequest(Long orderTableId, List<OrderLineItem> items) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(items);
        return order;
    }

    public static TableGroup createTableGroup(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }

    public static Product createProduct(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }

    public static Product createProduct(Long id, String name, int price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }

    public static OrderLineItem createOrderLineItem(long menuId, long orderId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static OrderLineItem createOrderLineItem(long menuId, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static Menu createMenu(String name, int price, long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static MenuProduct createMenuProduct(long menuId, long productId, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static MenuProduct createMenuProduct(long productId, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static MenuGroup createMenuGroup(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
