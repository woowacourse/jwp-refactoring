package kitchenpos.application.tablegroup;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroupGroupedEvent;
import kitchenpos.domain.tablegroup.TableGroupUngroupedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableGroupEventHandler {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupEventHandler(final OrderRepository orderRepository,
                                  final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void handleGroup(final TableGroupGroupedEvent event) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        for (final OrderTable orderTable : orderTables) {
            orderTable.group(event.getTableGroupId());
        }
    }

    @EventListener
    public void handleUngroup(final TableGroupUngroupedEvent event) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }
}
