package kitchenpos.order.application.listner;

import kitchenpos.order.application.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.exception.OrderException;
import kitchenpos.order.domain.exception.OrderExceptionType;
import kitchenpos.table.application.TableGroupUnGroupValidationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TableGroupUnGroupEventListener {

    private final OrderRepository orderRepository;

    public TableGroupUnGroupEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    @EventListener(TableGroupUnGroupValidationEvent.class)
    public void handle(final TableGroupUnGroupValidationEvent event) {
        final boolean orderStatusIsNotCompletion = orderRepository
            .findByOrderTableIdIn(event.getOrderTableIds())
            .stream()
            .anyMatch(Order::isNotAlreadyCompletion);

        if (orderStatusIsNotCompletion) {
            throw new OrderException(OrderExceptionType.ORDER_IS_NOT_COMPLETION);
        }
    }
}
