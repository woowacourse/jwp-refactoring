package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTableEmptyChangeEvent;
import kitchenpos.domain.repository.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final OrderRepository orderRepository;

    public OrderEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    void validatePossibleToChangeOrderTableEmpty(final OrderTableEmptyChangeEvent event) {
        orderRepository.findAllByOrderTableId(event.getOrderTableId())
                .forEach(Order::validatePossibleToChangeEmptyOfOrderTable);
    }
}
