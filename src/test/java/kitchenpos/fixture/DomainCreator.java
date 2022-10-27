package kitchenpos.fixture;

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

public class DomainCreator {

    public static Menu createMenu(final Long id, final String name, final BigDecimal price,
        final Long menuGroupId, final List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }

    public static MenuGroup createMenuGroup(final Long id, final String name) {
        return new MenuGroup(id, name);
    }

    public static Product createProduct(final Long id, final String name, final BigDecimal price) {
        return new Product(id, name, price);
    }

    public static MenuProduct createMenuProduct(final Long seq, final Long menuId,
        final Long productId, final int quantity) {
        return new MenuProduct(seq, menuId, productId, quantity);
    }

    public static OrderTable createOrderTable(final Long id, final Long tableGroupId,
        final int numberOfGuests, final boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public static TableGroup createTableGroup(final Long id, final List<OrderTable> orderTables) {
        return new TableGroup(id, LocalDateTime.now(), orderTables);
    }

    public static Order createOrder(final Long id, final Long orderTableId,
        final String orderStatus,
        final LocalDateTime orderedTime,
        final List<OrderLineItem> orderLineItems) {
        final Order order = new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.updateOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);
        orderLineItems
            .forEach(it -> it.setOrderId(id));
        order.setOrderLineItems(orderLineItems);

        return order;
    }

    public static OrderLineItem createOrderLineItem(final Long seq, final Long orderId,
        final Long menuId, final int quantity) {
        return new OrderLineItem(seq, orderId, menuId, quantity);
    }
}
