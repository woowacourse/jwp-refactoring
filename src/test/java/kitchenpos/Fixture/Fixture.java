package kitchenpos.Fixture;

import kitchenpos.domain.Product;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.ui.dto.OrderTableDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public abstract class Fixture {
    public static Menu menuFixture(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        Menu menu = new Menu(name, price, menuGroup);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static MenuGroup menuGroupFixture(String name) {
        return new MenuGroup(name);
    }

    public static MenuProduct menuProductFixture(Menu menu, Product product, long quantity) {
        return new MenuProduct(null, menu, product, quantity);
    }

    public static Product productFixture(String name, BigDecimal price) {
        return new Product(null, name, price);
    }

    public static Order orderFixture(OrderTable orderTable, String orderStatus,
                                     LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        Order order = new Order(orderTable, orderStatus, orderedTime);
        order.addOrderLineItems(orderLineItems);
        return order;
    }

    public static OrderLineItem orderLineItemFixture(Order order, Menu menu, long quantity) {
        return new OrderLineItem(null, order, menu, quantity);
    }

    public static OrderTable orderTableFixture(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }

    public static OrderTableDto orderTableDtoFixture(Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTableDto(tableGroupId, numberOfGuests, empty);
    }

    public static TableGroup tableGroupFixture(LocalDateTime createdDate, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
