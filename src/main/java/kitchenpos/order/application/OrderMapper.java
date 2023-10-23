package kitchenpos.order.application;

import kitchenpos.order.application.dto.OrderCreationRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final OrderValidator orderValidator;

    public OrderMapper(final OrderValidator orderValidator) {
        this.orderValidator = orderValidator;
    }

    public Order from(final OrderCreationRequest request) {
        final Long orderTableId = request.getOrderTableId();
        new Order(orderTableId, orderValidator)
    }
}
