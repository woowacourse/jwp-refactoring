package kitchenpos.ui.converter;

import kitchenpos.domain.order.OrderStatus;
import org.springframework.core.convert.converter.Converter;

public class OrderStatusConverter implements Converter<String, OrderStatus> {
    @Override
    public OrderStatus convert(String source) {
        return OrderStatus.of(source);
    }
}
