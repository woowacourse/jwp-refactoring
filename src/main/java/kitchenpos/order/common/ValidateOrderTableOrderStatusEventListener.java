package kitchenpos.order.common;

import kitchenpos.common.ValidateOrderTableOrderStatusEvent;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ValidateOrderTableOrderStatusEventListener {

    private final OrderRepository orderRepository;

    public ValidateOrderTableOrderStatusEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void validateOrderStatus(final ValidateOrderTableOrderStatusEvent validateOrderTableOrderStatusEvent) {
        orderRepository.findAllByOrderTableId(validateOrderTableOrderStatusEvent.getOrderTableId())
                .forEach(Order::validateOrderComplete);
    }
}
