package kitchenpos.order.domain;

import java.util.Arrays;
import kitchenpos.order.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableValidator;
import kitchenpos.ordertable.exception.OrderTableException;
import kitchenpos.ordertable.exception.OrderTableException.CannotChangeEmptyStateByOrderStatusException;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
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
