package kitchenpos.order.domain;

import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderCompletion(final Long orderTableId) {
        final boolean isNotComplete = orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, OrderStatus.notCompleteStatuses()
        );
        if (isNotComplete) {
            throw new IllegalArgumentException();
        }
    }
}
