package kitchenpos.order.common;

import kitchenpos.common.ValidateOrderTablesOrderStatusEvent;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ValidateOrderTablesOrderStatusEventListener {

    private final OrderRepository orderRepository;

    public ValidateOrderTablesOrderStatusEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void validateOrdersStatus(final ValidateOrderTablesOrderStatusEvent validateOrderTablesOrderStatusEvent) {
        orderRepository.findByOrderTableIdIn(validateOrderTablesOrderStatusEvent.getOrderTableIds())
                .forEach(Order::validateOrderComplete);
    }
}
