package kitchenpos.ordertable.domain;

import java.util.Arrays;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.exception.OrderTableException;
import kitchenpos.ordertable.exception.OrderTableException.CannotChangeEmptyStateByOrderStatusException;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmpty(final Long orderTableId, final OrderTable orderTable) {
        if (orderTable.isExistTableGroup()) {
            throw new OrderTableException.AlreadyExistTableGroupException();
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new CannotChangeEmptyStateByOrderStatusException();
        }
    }
}
