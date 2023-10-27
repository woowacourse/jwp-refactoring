package kitchenpos.ordertable.domain.validator;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderTableValidator {
    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateCompletedOrderTable(final OrderTable orderTable) {
        final List<OrderStatus> inProgressingOrderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
        if (orderRepository.existsByOrderTableAndOrderStatusIn(orderTable, inProgressingOrderStatuses)) {
            throw new IllegalArgumentException();
        }
    }
}
