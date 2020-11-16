package kitchenpos.domain.model.ordertable;

import static java.util.Arrays.*;
import static java.util.Objects.*;

import org.springframework.stereotype.Component;

import kitchenpos.domain.model.order.OrderRepository;
import kitchenpos.domain.model.order.OrderStatus;

@Component
public class ChangeOrderTableEmptyVerifier {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public ChangeOrderTableEmptyVerifier(OrderTableRepository orderTableRepository,
            OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public OrderTable toOrderTable(Long id) {
        final OrderTable saved = orderTableRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        if (nonNull(saved.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(id,
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        return saved;
    }
}
