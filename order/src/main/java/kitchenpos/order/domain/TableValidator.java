package kitchenpos.order.domain;

import java.util.Arrays;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderValidator;
import org.springframework.stereotype.Component;

@Component
public class TableValidator implements OrderValidator {
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
