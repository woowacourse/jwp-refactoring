package kitchenpos.order.application;

import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    private final OrderRepository orderRepository;

    public OrderEventHandler(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
