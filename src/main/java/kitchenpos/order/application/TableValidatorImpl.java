package kitchenpos.order.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.application.TableValidator;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class TableValidatorImpl implements TableValidator {

    private final OrderRepository orderRepository;

    public TableValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateTableChangeEmpty(OrderTable orderTable) {
        if (orderTable.isBelongToTableGroup()) {
            throw new IllegalArgumentException();
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void validateUnGroupable(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
