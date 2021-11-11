package kitchenpos.fixtures;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderFixtures {

    private static final long ORDER_TABLE_ID = 1L;
    private static final OrderStatus ORDER_STATUS = OrderStatus.COOKING;
    private static final long ORDER_ID = 1L;
    private static final long QUANTITY = 1L;

    public static Order createOrder(
        Long id,
        OrderTable orderTable,
        OrderStatus orderStatus,
        List<OrderLineItem> orderLineItems
    ) {
        return new Order(id, orderTable, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public static Order createOrder(OrderStatus status) {
        OrderTable orderTable = new OrderTable(ORDER_TABLE_ID, null, new ArrayList<>(), 10, false);
        Order order = createOrder(ORDER_ID, orderTable, status, createOrderLineItems());
        orderTable.addOrder(order);
        return order;
    }

    public static Order createOrder() {
        return createOrder(ORDER_STATUS);
    }

    public static List<Order> createCompletedOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(createOrder(OrderStatus.COMPLETION));
        orders.add(createOrder(OrderStatus.COMPLETION));
        return orders;
    }

    public static List<Order> createMealOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(createOrder(OrderStatus.MEAL));
        orders.add(createOrder(OrderStatus.MEAL));
        return orders;
    }

    public static OrderRequest createOrderRequest(Order order) {
        return new OrderRequest(
            order.getOrderTable().getId(),
            order.getOrderStatus(),
            order.getOrderLineItems().stream()
                .map(item -> new OrderLineItemRequest(item.getMenu().getId(), item.getQuantity()))
                .collect(Collectors.toList())
        );
    }

    public static OrderRequest createOrderRequest() {
        return createOrderRequest(createOrder());
    }

    public static List<OrderLineItem> createOrderLineItems() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(createOrderLineItem());
        orderLineItems.add(createOrderLineItem());
        return orderLineItems;
    }

    public static OrderLineItem createOrderLineItem(
        Long seq,
        Order order,
        Menu menu,
        long quantity
    ) {
        return new OrderLineItem(seq, order, menu, quantity);
    }

    public static OrderLineItem createOrderLineItem() {
        return createOrderLineItem(null, null, MenuFixtures.createMenu(), QUANTITY);
    }
}
