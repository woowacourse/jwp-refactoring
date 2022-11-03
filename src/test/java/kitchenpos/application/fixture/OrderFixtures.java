package kitchenpos.application.fixture;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderChangeOrderStatusRequest;
import kitchenpos.dto.OrderLineItemSaveRequest;
import kitchenpos.dto.OrderSaveRequest;

public class OrderFixtures {

    public static final Order generateOrder(final Long orderTableId,
                                            final OrderStatus orderStatus,
                                            final List<OrderLineItem> orderLineItems) {
        return generateOrder(null, orderTableId, orderStatus.name(), LocalDateTime.now(), orderLineItems);
    }

    public static final Order generateOrder(final Long id, final Order order) {
        return generateOrder(
                id,
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                order.getOrderLineItems()
        );
    }

    public static final Order generateOrder(final Long id,
                                            final Long orderTableId,
                                            final String orderStatus,
                                            final LocalDateTime orderedTime,
                                            final List<OrderLineItem> orderLineItems) {
        try {
            Constructor<Order> constructor = Order.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            Order order = constructor.newInstance();
            Class<? extends Order> clazz = order.getClass();

            Field idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(order, id);

            Field orderTableIdField = clazz.getDeclaredField("orderTableId");
            orderTableIdField.setAccessible(true);
            orderTableIdField.set(order, orderTableId);

            Field orderStatusField = clazz.getDeclaredField("orderStatus");
            orderStatusField.setAccessible(true);
            orderStatusField.set(order, orderStatus);

            Field orderedTimeField = clazz.getDeclaredField("orderedTime");
            orderedTimeField.setAccessible(true);
            orderedTimeField.set(order, orderedTime);

            Field orderLineItemsField = clazz.getDeclaredField("orderLineItems");
            orderLineItemsField.setAccessible(true);
            orderLineItemsField.set(order, orderLineItems);

            return order;
        } catch (final Exception e) {
            throw new RuntimeException();
        }
    }

    public static final OrderSaveRequest generateOrderSaveRequest(final Long orderTableId,
                                                                  final List<OrderLineItemSaveRequest> requests) {
        return new OrderSaveRequest(orderTableId, requests);
    }

    public static final OrderChangeOrderStatusRequest generateOrderChangeOrderStatusRequest(
            final OrderStatus orderStatus
    ) {
        return new OrderChangeOrderStatusRequest(orderStatus.name());
    }
}
