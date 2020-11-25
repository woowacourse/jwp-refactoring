package kitchenpos.application.verifier;

import static java.util.Arrays.*;

import org.springframework.stereotype.Component;

import kitchenpos.domain.model.order.OrderRepository;
import kitchenpos.domain.model.order.OrderStatus;
import kitchenpos.domain.model.ordertable.OrderTable;
import kitchenpos.domain.model.ordertable.OrderTableRepository;

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

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(id,
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        return saved;
    }
}
