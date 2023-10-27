package kitchenpos.common.event;

import kitchenpos.order.service.OrderService;
import kitchenpos.table.event.ValidateAllOrderCompletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final OrderService orderService;

    public OrderEventListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @EventListener
    public void validateOrdersCompleted(ValidateAllOrderCompletedEvent event) {
        orderService.validateOrdersCompleted(event.getOrderTableId());
    }
}
