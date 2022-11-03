package kitchenpos.application.fixture;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Price;

public class OrderLineItemFixtures {

    public static final OrderLineItem generateOrderLineItem(final String name,
                                                            final BigDecimal price,
                                                            final long quantity) {
        return generateOrderLineItem(null, name, price, quantity);
    }

    public static final OrderLineItem generateOrderLineItem(final Long seq,
                                                            final String name,
                                                            final BigDecimal price,
                                                            final long quantity) {
        try {
            Constructor<OrderLineItem> constructor = OrderLineItem.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            OrderLineItem orderLineItem = constructor.newInstance();
            Class<? extends OrderLineItem> clazz = orderLineItem.getClass();

            Field seqField = clazz.getDeclaredField("seq");
            seqField.setAccessible(true);
            seqField.set(orderLineItem, seq);

            Field nameField = clazz.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(orderLineItem, name);

            Field priceField = clazz.getDeclaredField("price");
            priceField.setAccessible(true);
            priceField.set(orderLineItem, new Price(price));

            Field quantityField = clazz.getDeclaredField("quantity");
            quantityField.setAccessible(true);
            quantityField.set(orderLineItem, quantity);

            return orderLineItem;
        } catch (final Exception e) {
            throw new RuntimeException();
        }
    }
}
