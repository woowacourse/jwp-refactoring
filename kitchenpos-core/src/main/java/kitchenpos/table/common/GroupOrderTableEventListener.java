package kitchenpos.table.common;

import kitchenpos.common.GroupOrderTablesEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.DuplicateOrderTableException;
import kitchenpos.table.exception.InvalidOrderTableException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class GroupOrderTableEventListener {

    private final OrderTableRepository orderTableRepository;

    public GroupOrderTableEventListener(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void group(final GroupOrderTablesEvent groupOrderTablesEvent) {
        final List<Long> orderTableIds = groupOrderTablesEvent.getOrderTableIds();
        final List<OrderTable> orderTables = orderTableRepository.findByIdIn(orderTableIds);

        validateOrderTable(orderTables, orderTableIds);

        orderTables.forEach(orderTable -> orderTable.changeTableGroup(groupOrderTablesEvent.getTableGroupId()));
    }

    private void validateOrderTable(final List<OrderTable> orderTables, final List<Long> orderTableIds) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new InvalidOrderTableException();
        }

        for (final OrderTable savedOrderTable : orderTables) {
            if (savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new DuplicateOrderTableException();
            }
        }
    }
}
