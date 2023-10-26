package kitchenpos.ordertable.application;

import java.util.List;
import kitchenpos.common.event.TablesGroupedEvent;
import kitchenpos.common.event.TablesUngroupedEvent;
import kitchenpos.ordertable.domain.OrderStatusChecker;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.TableGroup;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupEventListener {

    private final OrderTableRepository orderTableRepository;
    private final OrderStatusChecker orderStatusChecker;

    public TableGroupEventListener(OrderTableRepository orderTableRepository, OrderStatusChecker orderStatusChecker) {
        this.orderTableRepository = orderTableRepository;
        this.orderStatusChecker = orderStatusChecker;
    }

    @EventListener
    public void groupTable(TablesGroupedEvent tablesGroupedEvent) {
        List<OrderTable> orderTables = fetchOrderTables(tablesGroupedEvent.getOrderTableIds());

        TableGroup tableGroup = TableGroup.group(tablesGroupedEvent.getTableGroupId(), orderTables);
        orderTableRepository.saveAll(tableGroup.getOrderTables());
    }

    @EventListener
    public void ungroupTable(TablesUngroupedEvent tablesUngroupedEvent) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(
                tablesUngroupedEvent.getTableGroupId());
        validateOrderStatus(orderTables);

        orderTables.forEach(OrderTable::ungroup);
        orderTableRepository.saveAll(orderTables);
    }

    private List<OrderTable> fetchOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }

        return orderTables;
    }

    private void validateOrderStatus(List<OrderTable> orderTables) {
        boolean hasIncompleteOrders = orderTables.stream()
                .map(OrderTable::getId)
                .anyMatch(orderStatusChecker::checkIncompleteOrders);

        if (hasIncompleteOrders) {
            throw new IllegalArgumentException();
        }
    }
}
