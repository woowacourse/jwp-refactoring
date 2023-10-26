package kitchenpos.table.domain;

import java.util.List;
import kitchenpos.tablegroup.domain.TableGroupingEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderTableGroupedEventHandler {

    private static final int MINIMUM_TABLE_SIZE = 2;

    private final OrderTableRepository orderTableRepository;

    public OrderTableGroupedEventHandler(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void handle(final TableGroupingEvent event) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(event.getOrderTableIds());
        validateOrderTableExist(event.getOrderTableIds(), orderTables);
        validateGrouping(orderTables);
        orderTables.forEach(orderTable -> orderTable.groupByTableGroup(event.getTableGroupId()));
    }

    private void validateOrderTableExist(final List<Long> eventOrderTableIds, final List<OrderTable> orderTables) {
        if (eventOrderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("Order table does not exist.");
        }
    }

    private void validateGrouping(final List<OrderTable> orderTables) {
        validateSize(orderTables);
        validateGroupOrderTableIsAvailable(orderTables);
    }

    private void validateSize(final List<OrderTable> orderTables) {
        if (orderTables.isEmpty() || orderTables.size() < MINIMUM_TABLE_SIZE) {
            throw new IllegalArgumentException("Table group must have at least two tables.");
        }
    }

    private void validateGroupOrderTableIsAvailable(final List<OrderTable> orderTables) {
        if (!isOrderTablesAbleToGroup(orderTables)) {
            throw new IllegalArgumentException("Cannot group non-empty table or already grouped table.");
        }
    }

    private boolean isOrderTablesAbleToGroup(final List<OrderTable> orderTables) {
        return orderTables.stream().allMatch(OrderTable::isAbleToGroup);
    }
}
