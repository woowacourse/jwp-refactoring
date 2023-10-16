package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTableChangedEvent;
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
