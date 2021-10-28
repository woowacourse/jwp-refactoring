package kitchenpos.utils;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DomainFactory {
    public static Menu CREATE_MENU(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(id, name, price, menuGroup);
    }

    public static MenuGroup CREATE_MENU_GROUP(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static Order CREATE_ORDER(Long id, OrderTable orderTable, String orderStatus) {
        return new Order(id, orderTable, orderStatus, LocalDateTime.now());
    }

    public static OrderLineItem CREATE_ORDER_LINE_ITEM(Long seq, Order order, Menu menu, Long quantity) {
        return new OrderLineItem(seq, order, menu, quantity);
    }

    public static OrderTable CREATE_ORDER_TABLE(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public static TableGroup CREATE_TABLE_GROUP(Long id) {
        return new TableGroup(id, LocalDateTime.now());
    }

    public static Product CREATE_PRODUCT(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

    public static MenuProduct CREATE_MENU_PRODUCT(Long seq, Menu menu, Product product, Long quantity) {
        return new MenuProduct(seq, menu, product, quantity);
    }
}
