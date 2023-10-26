package kitchenpos.order;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import kitchenpos.exception.OrderException.InvalidOrderStatusChangeException;
import kitchenpos.table.OrderStatusChecker;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusCheckerImpl implements OrderStatusChecker {

    private final OrderRepository orderRepository;

    public OrderStatusCheckerImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatusChangeable(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderStatusChangeException();
        }
    }

    @Override
    public boolean checkEnableUngroupingTableGroup(final Long tableGroupId) {
        final Set<Order> findOrders = orderRepository.findAllByTableGroupId(tableGroupId);

        return findOrders.stream()
            .anyMatch(order -> order.isSameStatus(OrderStatus.COOKING)
                || order.isSameStatus(OrderStatus.MEAL));
    }
}
