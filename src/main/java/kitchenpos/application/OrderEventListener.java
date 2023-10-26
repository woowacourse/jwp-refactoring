package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTableUpdateEvent;
import kitchenpos.domain.repository.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderEventListener {

    private final OrderRepository orderRepository;

    public OrderEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional
    public void validatePossibleToUpdateOrderTable(final OrderTableUpdateEvent event) {
        orderRepository.findAllByOrderTableId(event.getOrderTableId())
                .forEach(Order::validatePossibleToUpdateOrderTable);
    }
}
