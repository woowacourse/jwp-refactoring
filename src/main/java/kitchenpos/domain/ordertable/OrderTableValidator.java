package kitchenpos.domain.ordertable;

import kitchenpos.domain.order.Order;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
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
