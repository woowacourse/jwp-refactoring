package kitchenpos.common;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

public class TestObjectUtils {
    private TestObjectUtils() {
    }

    public static Product createProduct(Long id, String name, BigDecimal price) {
        Product product = new Product();
        ReflectionTestUtils.setField(product, "id", id);
        ReflectionTestUtils.setField(product, "name", name);
        ReflectionTestUtils.setField(product, "price", price);

        return product;
    }

    public static MenuGroup createMenuGroup(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        ReflectionTestUtils.setField(menuGroup, "id", id);
        ReflectionTestUtils.setField(menuGroup, "name", name);

        return menuGroup;
    }

    public static Menu createMenu(Long id, String name, BigDecimal price, Long menuGroupId,
            List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        ReflectionTestUtils.setField(menu, "id", id);
        ReflectionTestUtils.setField(menu, "name", name);
        ReflectionTestUtils.setField(menu, "price", price);
        ReflectionTestUtils.setField(menu, "menuGroupId", menuGroupId);
        ReflectionTestUtils.setField(menu, "menuProducts", menuProducts);

        return menu;
    }

    public static MenuProduct createMenuProduct(Long seq, Long menuId, Long productId,
            Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        ReflectionTestUtils.setField(menuProduct, "seq", seq);
        ReflectionTestUtils.setField(menuProduct, "menuId", menuId);
        ReflectionTestUtils.setField(menuProduct, "productId", productId);
        ReflectionTestUtils.setField(menuProduct, "quantity", quantity);

        return menuProduct;
    }

    public static OrderTable createOrderTable(Long id, Long tableGroupId, int numberOfGuests,
            boolean empty) {
        OrderTable orderTable = new OrderTable();
        ReflectionTestUtils.setField(orderTable, "id", id);
        ReflectionTestUtils.setField(orderTable, "tableGroupId", tableGroupId);
        ReflectionTestUtils.setField(orderTable, "numberOfGuests", numberOfGuests);
        ReflectionTestUtils.setField(orderTable, "empty", empty);

        return orderTable;
    }

    public static Order createOrder(Long id, Long orderTableId, String orderStatus,
            LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        ReflectionTestUtils.setField(order, "id", id);
        ReflectionTestUtils.setField(order, "orderTableId", orderTableId);
        ReflectionTestUtils.setField(order, "orderStatus", orderStatus);
        ReflectionTestUtils.setField(order, "orderedTime", orderedTime);
        ReflectionTestUtils.setField(order, "orderLineItems", orderLineItems);

        return order;
    }

    public static OrderLineItem createOrderLineItem(Long seq, Long orderId, Long menuId,
            long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        ReflectionTestUtils.setField(orderLineItem, "seq", seq);
        ReflectionTestUtils.setField(orderLineItem, "orderId", orderId);
        ReflectionTestUtils.setField(orderLineItem, "menuId", menuId);
        ReflectionTestUtils.setField(orderLineItem, "quantity", quantity);

        return orderLineItem;
    }

    public static TableGroup createTableGroup(Long id, LocalDateTime createdDate,
            List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        ReflectionTestUtils.setField(tableGroup, "id", id);
        ReflectionTestUtils.setField(tableGroup, "createdDate", createdDate);
        ReflectionTestUtils.setField(tableGroup, "orderTables", orderTables);

        return tableGroup;
    }
}
