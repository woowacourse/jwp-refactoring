package kitchenpos.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

public class ObjectUtil {
    public static Menu createMenu(Long id, String name, Integer price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        BigDecimal value = null;
        if (price != null) {
            value = BigDecimal.valueOf(price);
        }
        return Menu.builder()
            .id(id)
            .name(name)
            .price(value)
            .menuGroupId(menuGroupId)
            .menuProducts(menuProducts)
            .build();
    }

    public static MenuGroup createMenuGroup(Long id, String name) {
        return MenuGroup.builder()
            .id(id)
            .name(name)
            .build();
    }

    public static MenuProduct createMenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        return MenuProduct.builder()
            .seq(seq)
            .menuId(menuId)
            .productId(productId)
            .quantity(quantity)
            .build();
    }

    public static Order createOrder(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems) {
        return Order.builder()
            .id(id)
            .orderTableId(orderTableId)
            .orderStatus(orderStatus)
            .orderedTime(orderedTime)
            .orderLineItems(orderLineItems)
            .build();
    }

    public static OrderLineItem createOrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        return OrderLineItem.builder()
            .seq(seq)
            .orderId(orderId)
            .menuId(menuId)
            .quantity(quantity)
            .build();
    }

    public static Product createProduct(Long id, String name, Integer price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        if (price != null) {
            product.setPrice(BigDecimal.valueOf(price));
        }
        return product;
    }

    public static OrderTable createOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static TableGroup createTableGroup(Long id, LocalDateTime createdDateTime, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDateTime);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
