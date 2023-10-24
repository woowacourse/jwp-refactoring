package kitchenpos.order.application.listner;

import kitchenpos.order.application.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.exception.OrderException;
import kitchenpos.order.domain.exception.OrderExceptionType;
import kitchenpos.table.application.TableChangeEmptyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TableChangeEmptyEventHandler {

    private final OrderRepository orderRepository;

    public TableChangeEmptyEventHandler(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    @EventListener(TableChangeEmptyEvent.class)
    public void handle(final TableChangeEmptyEvent event) {
        orderRepository.findByOrderTableId(event.getOrderTableId())
            .ifPresent(this::validateOrderIsNotCompletion);
    }

    private void validateOrderIsNotCompletion(final Order order) {
        if (order.isNotAlreadyCompletion()) {
            throw new OrderException(OrderExceptionType.ORDER_IS_NOT_COMPLETION);
        }
    }
}
