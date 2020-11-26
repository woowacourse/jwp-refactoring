package kitchenpos.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.OrderChangeRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderTableChangeRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.TableGroupCreateRequest;

public class TestObjectFactory {

    public static MenuGroupCreateRequest createMenuGroupCreateRequest(String menuName) {
        MenuGroup menuGroup = new MenuGroup(null, menuName);

        return MenuGroupCreateRequest.of(menuGroup);
    }

    public static MenuCreateRequest createMenuCreateRequest(String name, BigDecimal price,
        MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        Menu menu = new Menu(name, Price.of(price), menuGroup);

        return MenuCreateRequest.of(menu, menuProducts);
    }

    public static ProductCreateRequest createProductCreateRequest(String name, BigDecimal price) {
        Product product = new Product(name, Price.of(price));

        return ProductCreateRequest.of(product);
    }

    public static OrderCreateRequest createOrderCreateRequest(OrderTable orderTable,
        String orderStatus, List<OrderLineItem> orderLineItems) {
        Order order = Order.of(orderTable, OrderStatus.valueOf(orderStatus), LocalDateTime.now());

        return OrderCreateRequest.of(order, orderLineItems);
    }

    public static OrderChangeRequest createOrderChangeRequest(String orderStatus) {
        Order order = Order.of(new OrderTable(), OrderStatus.valueOf(orderStatus), null);

        return OrderChangeRequest.of(order);
    }

    public static OrderLineItem createOrderLineItem(Menu menu, long quantity) {
        return new OrderLineItem(null, null, menu, quantity);
    }

    public static TableGroupCreateRequest createTableGroupCreateRequest(
        List<OrderTableResponse> orderTableResponses) {
        List<OrderTable> orderTables = new ArrayList<>();

        for (OrderTableResponse orderTableResponse : orderTableResponses) {
            Long id = orderTableResponse.getId();
            int numberOfGuests = orderTableResponse.getNumberOfGuests();
            boolean empty = orderTableResponse.isEmpty();

            orderTables.add(new OrderTable(id, null, numberOfGuests, empty));
        }

        return TableGroupCreateRequest.of(orderTables);
    }

    public static OrderTableCreateRequest createOrderTableCreateRequest(int numberOfGuests,
        boolean empty) {
        OrderTable orderTable = OrderTable.of(null, numberOfGuests, empty);

        return OrderTableCreateRequest.of(orderTable);
    }

    public static OrderTableChangeRequest createOrderTableChangeRequest(int numberOfGuests,
        boolean empty) {
        OrderTable orderTable = OrderTable.of(null, numberOfGuests, empty);

        return OrderTableChangeRequest.of(orderTable);
    }
}
