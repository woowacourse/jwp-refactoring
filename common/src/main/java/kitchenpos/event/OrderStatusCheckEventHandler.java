package kitchenpos.event;

import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.table.OrderStatusCheckEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusCheckEventHandler implements ApplicationListener<OrderStatusCheckEvent> {
    private final OrderRepository orderRepository;

    public OrderStatusCheckEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void onApplicationEvent(OrderStatusCheckEvent event) {
        Long orderTableId = event.getOrderTableId();
        Orders orders = new Orders(orderRepository.findAllByOrderTableId(orderTableId));
        orders.checkNotCompleted();
    }
}
