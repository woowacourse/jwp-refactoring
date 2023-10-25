package kitchenpos.common;

import kitchenpos.application.order.OrderService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private OrderService orderService;

    public OrderEventListener(final OrderService orderService) {
        this.orderService = orderService;
    }

    @EventListener
    public void validateOrderStatus(ValidateOrderStatusEvent event) {
        orderService.validateOrderStatus(event.getOrderTableId());
    }
}
