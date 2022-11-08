package kitchenpos.domain.ordertable;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class TableValidatorImpl implements TableValidator {

    private final OrderRepository orderRepository;

    public TableValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateUnGroupCondition(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void validateUnGroupCondition(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
