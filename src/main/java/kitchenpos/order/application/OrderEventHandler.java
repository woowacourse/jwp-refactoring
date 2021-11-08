package kitchenpos.order.application;

import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.application.CheckAllOrderCompletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    private final OrderRepository orderRepository;

    public OrderEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void checkAllOrderCompleted(CheckAllOrderCompletedEvent checkAllOrderCompletedEvent) {
        final Long orderTableId = checkAllOrderCompletedEvent.getOrderTableId();
        final Orders orders = new Orders(orderRepository.findAllByOrderTableId(orderTableId));
        orders.checkAllOrderCompleted();
    }
}
