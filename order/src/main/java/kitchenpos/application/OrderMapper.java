package kitchenpos.application;

import kitchenpos.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toOrder(Long orderTableId, OrderValidator validator) {
        validator.validate(orderTableId);

        return new Order(orderTableId);
    }
}
