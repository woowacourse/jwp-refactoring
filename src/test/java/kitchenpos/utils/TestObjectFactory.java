package kitchenpos.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuProductResponse;

public class TestObjectFactory {

    public static MenuGroup createMenuGroup(String menuName) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(menuName);

        return menuGroup;
    }

    public static MenuCreateRequest createMenuCreateReqeust(String name, BigDecimal price,
        MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        List<MenuProductRequest> menuProductResponses = menuProducts.stream()
            .map(mp -> MenuProductRequest.of(mp))
            .collect(Collectors.toList());

        return new MenuCreateRequest(name, price, menuGroup.getId(), menuProductResponses);
    }

    public static MenuProduct createMenuProduct(Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProduct(product);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    public static Product createProduct(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return product;
    }

    public static Order createOrder(OrderTable orderTable, String orderStatus, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTable(orderTable);
        order.setOrderStatus(orderStatus);
        order.setOrderLineItems(orderLineItems);

        return order;
    }

    public static OrderLineItem createOrderLineItem(Menu menu, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenu(menu);
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }

    public static TableGroup createTableGroup(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        return tableGroup;
    }

    public static OrderTable createOrderTable(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }
}
