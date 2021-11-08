package kitchenpos.order.application;

import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.application.OrdersCompletedCheckEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    private final OrderRepository orderRepository;

    public OrderEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void checkAllOrderCompleted(OrdersCompletedCheckEvent ordersCompletedCheckEvent) {
        final Long orderTableId = ordersCompletedCheckEvent.getOrderTableId();
        final Orders orders = new Orders(orderRepository.findAllByOrderTableId(orderTableId));
        orders.checkAllOrderCompleted();
    }
}
