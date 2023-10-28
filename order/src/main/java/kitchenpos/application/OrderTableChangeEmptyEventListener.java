package kitchenpos.application;

import java.util.List;
import kitchenpos.exception.UnCompletedOrderExistsException;
import kitchenpos.order.domain.Order;
import kitchenpos.repository.OrderRepository;
import kitchenpos.table.domain.OrderTableChangeEmptyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OrderTableChangeEmptyEventListener {

    private final OrderRepository repository;

    public OrderTableChangeEmptyEventListener(OrderRepository repository) {
        this.repository = repository;
    }

    @EventListener
    public void handleOrderTableChangeEmptyEvent(OrderTableChangeEmptyEvent event) {
        List<Order> orders = repository.findAllByOrderTableId(event.getOrderTableId());
        validateDoesEveryOrderCompleted(orders);
    }

    private void validateDoesEveryOrderCompleted(List<Order> orders) {
        boolean isStatusNotChangeable = orders.stream()
                .anyMatch(Order::isOrderUnCompleted);
        if (isStatusNotChangeable) {
            throw new UnCompletedOrderExistsException();
        }
    }
}
