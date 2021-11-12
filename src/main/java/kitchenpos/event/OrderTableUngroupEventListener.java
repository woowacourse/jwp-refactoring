package kitchenpos.event;

import kitchenpos.order.domain.OrderTableUngroupEvent;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.repository.OrderRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OrderTableUngroupEventListener implements ApplicationListener<OrderTableUngroupEvent> {
    private final OrderRepository orderRepository;

    public OrderTableUngroupEventListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void onApplicationEvent(OrderTableUngroupEvent event) {
        System.out.println("here");
        Long orderTableId = event.getOrderTableId();
        Orders orders = new Orders(orderRepository.findAllByOrderTableId(orderTableId));
        orders.checkNotCompleted();
    }
}
