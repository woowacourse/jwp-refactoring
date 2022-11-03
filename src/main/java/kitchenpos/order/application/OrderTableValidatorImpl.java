package kitchenpos.order.application;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.application.OrderTableValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void checkOrderComplete(final Long orderTableId) {
        final Optional<Order> order = orderRepository.findByOrderTableId(orderTableId);
        if (order.isPresent() && !order.get().isComplete()) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void checkOrderComplete(final List<Long> orderTableIds) {
        final List<Order> orders = orderRepository.findByOrderTableIdIn(orderTableIds);
        final boolean isAllComplete = orders.stream()
                .allMatch(Order::isComplete);
        if (!isAllComplete) {
            throw new IllegalArgumentException();
        }
    }
}
