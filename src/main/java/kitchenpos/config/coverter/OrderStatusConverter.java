package kitchenpos.config.coverter;

import kitchenpos.order.OrderStatus;
import org.springframework.core.convert.converter.Converter;

public class OrderStatusConverter implements Converter<String, OrderStatus> {

    @Override
    public OrderStatus convert(final String source) {
        return OrderStatus.from(source);
    }
}
