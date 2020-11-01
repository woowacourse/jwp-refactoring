package kitchenpos.helper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Product;
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;

public class EntityCreateHelper {
    public static MenuGroup createMenuGroup(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }

    public static Menu createMenu(Long id, Long menuGroupId, List<MenuProduct> menuProducts, String name,
        BigDecimal price) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        menu.setName(name);
        menu.setPrice(price);
        return menu;
    }

    public static MenuProduct createMenuProduct(Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static OrderLineItem createOrderLineItem(Long orderId, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static Table createTable(Long id, boolean isEmpty, Long tableGroupId, int numberOfGuests) {
        Table table = new Table();
        table.setId(id);
        table.setEmpty(isEmpty);
        table.setTableGroupId(tableGroupId);
        table.setNumberOfGuests(numberOfGuests);
        return table;
    }

    public static Product createProduct(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static TableGroup createTableGroup(Long id, LocalDateTime createdDate, List<Table> tables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(tables);
        return tableGroup;
    }

    public static Order createOrder(Long id, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems,
        OrderStatus orderStatus, Long orderTableId) {
        Order order = new Order();
        order.setId(id);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);
        order.setOrderStatus(orderStatus.name());
        order.setOrderTableId(orderTableId);
        return order;
    }
}
