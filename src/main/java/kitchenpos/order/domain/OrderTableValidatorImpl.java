package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateOnChangeOrderTableEmpty(final OrderTable orderTable) {
        validateHasTableGroup(orderTable);
        validateExistsNotCompletedOrder(orderTable);
    }

    private void validateHasTableGroup(final OrderTable orderTable) {
        if (orderTable.getTableGroupId() != null) {
            throw new IllegalArgumentException();
        }
    }

    public void validateExistsNotCompletedOrder(final OrderTable orderTable) {
        boolean existsNotCompletedOrder = orderRepository.findByOrderTableId(orderTable.getId())
                .stream()
                .anyMatch(Order::isCompletion);

        if (existsNotCompletedOrder) {
            throw new IllegalArgumentException();
        }
    }
}
