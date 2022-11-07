package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.exception.OrderNotCompletionException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.OrderTableValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {

    private static final List<String> NOT_COMPLETION = Arrays.asList(OrderStatus.COOKING.name(),
            OrderStatus.MEAL.name());

    private final OrderRepository orderRepository;

    public OrderTableValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, NOT_COMPLETION)) {
            throw new OrderNotCompletionException();
        }
    }

    @Override
    public void validate(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, NOT_COMPLETION)) {
            throw new OrderNotCompletionException();
        }
    }
}
