package kitchenpos.application.common;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.menu.MenuProductDto;
import kitchenpos.dto.menu.MenuResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TestObjectFactory {
    public static OrderTable creatOrderTableDto() {
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

    public static Order createOrderDto(Long orderTableId, List<OrderLineItem> items) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(items);
        return order;
    }

    public static TableGroup createTableGroupDto(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }

    public static Product createProductDto(String name, int price) {
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

    public static MenuResponse createMenuResponse(Long id, String name, int price, long menuGroupId, List<MenuProductDto> menuProducts) {
        return new MenuResponse(id, name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

    public static MenuProductDto createMenuProductDto(long id, long menuId, long productId, int quantity) {
        return new MenuProductDto(id, menuId, productId, quantity);
    }

    public static MenuProduct createMenuProduct(long productId, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static MenuGroup createMenuGroupDto(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    public static Order createChangeOrderStatusDto(String orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus);
        return order;
    }
}
