package kitchenpos.order.domain.validator;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.validator.OrderTableValidator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderInOrderTableValidator implements OrderTableValidator {
    private final OrderRepository orderRepository;

    public OrderInOrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateCompletedOrderTable(final OrderTable orderTable) {
        final List<OrderStatus> inProgressingOrderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
        if (orderRepository.existsByOrderTableAndOrderStatusIn(orderTable, inProgressingOrderStatuses)) {
            throw new IllegalArgumentException();
        }
    }
}
