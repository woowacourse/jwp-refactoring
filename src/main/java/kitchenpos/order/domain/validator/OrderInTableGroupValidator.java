package kitchenpos.order.domain.validator;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.validator.TableGroupValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderInTableGroupValidator implements TableGroupValidator {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderInTableGroupValidator(final OrderTableRepository orderTableRepository,
                                      final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderTable(final List<Long> orderTableIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateExistAllOrderTables(orderTableIds, orderTables);
        validateOrderTableCountOverTwo(orderTables);
        for (final OrderTable orderTable : orderTables) {
            validateEmptyTable(orderTable);
        }
    }

    private static void validateOrderTableCountOverTwo(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateEmptyTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || orderTable.getTableGroupId() != null) {
            throw new IllegalArgumentException();
        }
    }

    private void validateExistAllOrderTables(final List<Long> orderTableIds,
                                             final List<OrderTable> findOrderTables) {
        if (orderTableIds.size() != findOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void validateCompletedOrderTableInTableGroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        final List<OrderStatus> inProgressingOrderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(orderTables, inProgressingOrderStatuses)) {
            throw new IllegalArgumentException();
        }
    }
}
