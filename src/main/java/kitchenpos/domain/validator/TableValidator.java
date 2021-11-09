package kitchenpos.domain.validator;

import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateUpdateEmpty(OrderTable orderTable) {

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
