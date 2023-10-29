package kitchenpos.order.domain;

import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.ValidateOrderOfTableEvent;
import kitchenpos.table_group.domain.ValidateOrderOfTablesEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {
    private final OrderRepository orderRepository;

    public OrderEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void validateAllOrdersCompletion(final ValidateOrderOfTableEvent event) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(event.getOrderTableId(),
                OrderStatus.NOT_COMPLETION_STATUSES)) {
            throw new IllegalArgumentException();
        }
    }

    @EventListener
    public void validateAllOrderTablesCompletion(final ValidateOrderOfTablesEvent event) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                event.getOrderTableIds(), OrderStatus.NOT_COMPLETION_STATUSES)) {
            throw new IllegalArgumentException();
        }
    }
}
