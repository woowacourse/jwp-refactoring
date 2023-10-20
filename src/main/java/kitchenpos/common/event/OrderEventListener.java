package kitchenpos.common.event;

import kitchenpos.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    @Autowired
    private OrderService orderService;

    @EventListener
    public void validateOrdersCompleted(ValidateOrdersCompletedEvent event) {
        orderService.validateOrdersCompleted(event.getOrderTableId());
    }
}
