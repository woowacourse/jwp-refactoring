package kitchenpos.order.application;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.application.OrderTableValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTableStatusValidator implements OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableStatusValidator(final OrderRepository orderRepository) {
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
        final boolean isNotAllComplete = orders.stream()
                .noneMatch(Order::isComplete);
        if (isNotAllComplete) {
            throw new IllegalArgumentException();
        }
    }
}
