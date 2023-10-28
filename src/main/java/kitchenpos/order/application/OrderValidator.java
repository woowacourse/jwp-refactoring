package kitchenpos.order.application;

import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import org.springframework.stereotype.Component;

import java.util.List;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

@Component
public class OrderValidator {
    private final OrderRepository orderRepository;

    public OrderValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(final Long orderTableId, final OrderTable savedOrderTable) {
        if (savedOrderTable.validateTableGroupIsNonNull()) {
            throw new IllegalArgumentException();
        }
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, List.of(COOKING, MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
