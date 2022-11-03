package kitchenpos.order.application;

import java.util.List;
import kitchenpos.exception.InvalidOrderStatusException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.application.OrderValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorImpl implements OrderValidator {
    private final OrderRepository orderRepository;

    public OrderValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderNotCompleted(final Long orderTableId) {
        orderRepository.findByOrderTableId(orderTableId)
                .ifPresent(this::validateOrderCompleted);
    }

    @Override
    public void validateAnyOrdersNotCompleted(final List<Long> orderTableIds) {

        final List<Order> foundOrders = orderRepository.findAllByOrderTableId(orderTableIds);
        foundOrders.forEach(this::validateOrderCompleted);
    }

    private void validateOrderCompleted(final Order order) {
        if (order.isNotComplete()) {
            throw new InvalidOrderStatusException();
        }
    }
}
