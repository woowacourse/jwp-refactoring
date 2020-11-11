package kitchenpos;

import kitchenpos.domain.menu.*;
import kitchenpos.domain.order.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class DomainFactory {
    public static MenuGroup createMenuGroup(String name) {
        return new MenuGroup(name);
    }

    public static Product createProduct(String name, BigDecimal price) {
        return new Product(name, Price.of(price));
    }

    public static MenuProduct createMenuProduct(Long menuId, Long productId, long quantity) {
        return new MenuProduct(new Menu(menuId), new Product(productId), quantity);
    }

    public static Menu createMenu(Long menuGroupId, String name, BigDecimal price) {
        return new Menu(name, Price.of(price), new MenuGroup(menuGroupId));
    }

    public static OrderTable createOrderTable(int numberOfGuests, boolean empty, Long tableGroupId) {
        if (Objects.nonNull(tableGroupId)) {
            return new OrderTable(new TableGroup(tableGroupId), NumberOfGuests.of(numberOfGuests), Empty.of(empty));
        }
        return new OrderTable(null, NumberOfGuests.of(numberOfGuests), Empty.of(empty));
    }

    public static OrderLineItem createOrderLineItem(Long orderId, Long menuId, long quantity) {
        return new OrderLineItem(new Order(orderId), new Menu(menuId), quantity);
    }

    public static Order createOrder(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        return new Order(new OrderTable(orderTableId), orderStatus, orderedTime);
    }

    public static TableGroup createTableGroup() {
        return new TableGroup(LocalDateTime.now());
    }
}
