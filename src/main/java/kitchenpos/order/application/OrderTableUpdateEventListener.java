package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableEmptyUpdateEvent;
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
