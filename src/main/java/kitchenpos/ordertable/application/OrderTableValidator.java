package kitchenpos.ordertable.application;

import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Component;

import java.util.List;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

@Component
public class OrderTableValidator {
    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
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
