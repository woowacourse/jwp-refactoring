package kitchenpos.application.common;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
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

    public static OrderTable createChangeEmptyOrderTable(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable createChangeNumberOfGuestsDto(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public static Order createOrder(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> items) {
        Order order = new Order();
        order.setOrderTable(orderTable);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(LocalDateTime.now());
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

    public static OrderLineItem createOrderLineItem(Menu menu, Order order, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenu(menu);
        orderLineItem.setOrder(order);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static MenuResponse createMenuResponse(Long id, String name, int price, long menuGroupId, List<MenuProductDto> menuProducts) {
        return new MenuResponse(id, name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

    public static MenuProductDto createMenuProductDto(long id, long menuId, long productId, int quantity) {
        return new MenuProductDto(id, menuId, productId, quantity);
    }

    public static MenuProductDto createMenuProductDto(long productId, long quantity) {
        return new MenuProductDto(productId, quantity);
    }

    public static MenuGroup createMenuGroupDto(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static Order createChangeOrderStatusDto(OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus);
        return order;
    }
}
