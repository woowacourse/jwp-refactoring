package kitchenpos.domain.order;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import kitchenpos.domain.exception.OrderException.InvalidOrderStatusChangeException;
import kitchenpos.domain.table.OrderStatusChecker;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusCheckerImpl implements OrderStatusChecker {

    private final OrderRepository orderRepository;

    public OrderStatusCheckerImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatusChangeable(final List<Long> orderIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderIds,
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderStatusChangeException();
        }
    }

    @Override
    public boolean checkUngroupableTableGroup(final Long tableGroupId) {
        final Set<Order> findOrders = orderRepository.findAllByTableGroupId(tableGroupId);

        return findOrders.stream()
            .anyMatch(orderTable -> orderTable.isSameStatus(OrderStatus.COOKING)
                || orderTable.isSameStatus(OrderStatus.MEAL));
    }
}
