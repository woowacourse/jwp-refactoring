package kitchenpos.domain.table;

import java.util.Arrays;
import java.util.Objects;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmpty(final OrderTable orderTable) {
        validateOrderStatus(orderTable);
        validateTableGroup(orderTable);
    }

    private void validateTableGroup(final OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderStatus(final OrderTable orderTable) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    public void validateChangeNumberOfGuests(final OrderTable orderTable) {
        validateNegativeNumberOfGuests(orderTable);
        validateEmpty(orderTable);
    }

    private void validateEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNegativeNumberOfGuests(final OrderTable orderTable) {
        if (orderTable.getNumberOfGuests() < 0) {
            throw new IllegalArgumentException();
        }
    }
}
