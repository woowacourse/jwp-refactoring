package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTableEmptyUpdateEvent;
import kitchenpos.domain.repository.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTableUpdateEventListener {

    private final OrderRepository orderRepository;

    public OrderTableUpdateEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional
    public void validatePossibleToUpdateOrderTable(final OrderTableEmptyUpdateEvent event) {
        orderRepository.findAllByOrderTableId(event.getOrderTableId())
                .forEach(Order::validatePossibleToUpdateOrderTable);
    }
}
