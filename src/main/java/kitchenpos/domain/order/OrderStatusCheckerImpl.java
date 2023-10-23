package kitchenpos.domain.order;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import kitchenpos.application.exception.TableGroupAppException.UngroupingNotPossibleException;
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
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void validateOrderStatusChangeableByTableGroupId(final Long tableGroupId) {
        final Set<Order> findOrders = orderRepository.findAllByTableGroupId(tableGroupId);

        if (findOrders.stream()
            .anyMatch(orderTable -> orderTable.isSameStatus(OrderStatus.COOKING)
                || orderTable.isSameStatus(OrderStatus.MEAL))) {
            throw new UngroupingNotPossibleException();
        }
    }
}
