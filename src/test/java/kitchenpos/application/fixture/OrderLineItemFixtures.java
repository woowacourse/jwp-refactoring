package kitchenpos.application.fixture;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixtures {

    public static final OrderLineItem generateOrderLineItem(final Long menuId, final long quantity) {
        return generateOrderLineItem(null, null, menuId, quantity);
    }

    public static final OrderLineItem generateOrderLineItem(final Long seq, final OrderLineItem orderLineItem) {
        return generateOrderLineItem(
                seq,
                orderLineItem.getOrderId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity()
        );
    }

    public static final OrderLineItem generateOrderLineItem(final Long seq,
                                                            final Long orderId,
                                                            final Long menuId,
                                                            final long quantity) {
        try {
            Constructor<OrderLineItem> constructor = OrderLineItem.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            OrderLineItem orderLineItem = constructor.newInstance();
            Class<? extends OrderLineItem> clazz = orderLineItem.getClass();

            Field seqField = clazz.getDeclaredField("seq");
            seqField.setAccessible(true);
            seqField.set(orderLineItem, seq);

            Field orderIdField = clazz.getDeclaredField("orderId");
            orderIdField.setAccessible(true);
            orderIdField.set(orderLineItem, orderId);

            Field menuIdField = clazz.getDeclaredField("menuId");
            menuIdField.setAccessible(true);
            menuIdField.set(orderLineItem, menuId);

            Field quantityField = clazz.getDeclaredField("quantity");
            quantityField.setAccessible(true);
            quantityField.set(orderLineItem, quantity);

            return orderLineItem;
        } catch (final Exception e) {
            throw new RuntimeException();
        }
    }
}
