package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.application.OrdersInTableCompleteValidator;
import kitchenpos.tablegroup.application.OrdersInTablesCompleteValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorImpl implements OrdersInTablesCompleteValidator, OrdersInTableCompleteValidator {

    private static final List<String> UNGROUPABLE_ORDER_STATUSES = List.of(
            OrderStatus.COOKING.name(),
            OrderStatus.MEAL.name()
    );

    private static final List<String> UNCHANGEABLE_ORDER_STATUSES = List.of(
            OrderStatus.COOKING.name(),
            OrderStatus.MEAL.name()
    );

    private final OrderRepository orderRepository;

    public OrderValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(final List<Long> tableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(tableIds, UNGROUPABLE_ORDER_STATUSES)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void validate(final Long tableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(tableId, UNCHANGEABLE_ORDER_STATUSES)) {
            throw new IllegalArgumentException();
        }
    }
}
