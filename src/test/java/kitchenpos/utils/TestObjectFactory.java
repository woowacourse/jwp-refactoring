package kitchenpos.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.OrderChangeRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.TableGroupCreateRequest;

public class TestObjectFactory {

    public static MenuGroupCreateRequest createMenuGroupCreateRequest(String menuName) {
        MenuGroup menuGroup = new MenuGroup(menuName);

        return MenuGroupCreateRequest.of(menuGroup);
    }

    public static MenuCreateRequest createMenuCreateRequest(String name, BigDecimal price,
        MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        Menu menu = new Menu(name, price, menuGroup, menuProducts);

        return MenuCreateRequest.of(menu);
    }

    public static MenuProduct createMenuProduct(Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProduct(product);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    public static ProductCreateRequest createProductCreateRequest(String name, BigDecimal price) {
        Product product = new Product(name, price);

        return ProductCreateRequest.of(product);
    }

    public static OrderCreateRequest createOrderCreateRequest(OrderTable orderTable,
        String orderStatus, List<OrderLineItem> orderLineItems) {
        Order order = new Order(orderTable, OrderStatus.valueOf(orderStatus), LocalDateTime.now(), orderLineItems);

        return OrderCreateRequest.of(order);
    }

    public static OrderChangeRequest createOrderChangeRequest(String orderStatus) {
        Order order = new Order(null, OrderStatus.valueOf(orderStatus), null);

        return OrderChangeRequest.of(order);
    }

    public static OrderLineItem createOrderLineItem(Menu menu, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenu(menu);
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }

    public static TableGroupCreateRequest createTableGroupCreateRequest(
        List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        return TableGroupCreateRequest.of(tableGroup);
    }

    public static OrderTable createOrderTable(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }
}
