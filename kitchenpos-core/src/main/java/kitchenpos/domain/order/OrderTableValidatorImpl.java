package kitchenpos.domain.order;

import java.util.Arrays;
import kitchenpos.domain.orertable.OrderTable;
import kitchenpos.domain.orertable.OrderTableValidator;
import kitchenpos.domain.orertable.exception.OrderTableException;
import kitchenpos.domain.orertable.exception.OrderTableException.CannotChangeEmptyStateByOrderStatusException;
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
