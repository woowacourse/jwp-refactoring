package kitchenpos.application.order;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTableChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTableChangedEventHandler {
    private final OrderRepository orderRepository;

    public OrderTableChangedEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    @EventListener(OrderTableChangedEvent.class)
    public void handle(OrderTableChangedEvent event) {
        orderRepository.findByOrderTableId(event.getOrderTableId())
                .ifPresent(Order::validateChangeTableStatusAllowed);
    }
}
