package kitchenpos.utils;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class DomainFactory {

    private DomainFactory() {
    }

    public static Menu createMenu(Long id, String name, BigDecimal price,
                                  Long menuGroupId, MenuProduct menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(Arrays.asList(menuProducts));
        return menu;
    }

    public static MenuProduct createMenuProduct(Long productId, Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(null);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static Product createProduct(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static OrderTable createOrderTable(int numberOfGuests, Long tableGroupId, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(null);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static Order createOrder(String orderStatus, Long tableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setId(null);
        order.setOrderStatus(orderStatus);
        order.setOrderTableId(tableId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static MenuGroup createMenuGroup(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(null);
        menuGroup.setName(name);
        return menuGroup;
    }

    public static OrderLineItem createOrderLineItem(Long menuId, Long orderId, Long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(null);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
