package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroupGroupedEvent;
import kitchenpos.domain.TableGroupUngroupedEvent;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
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

    @EventListener(TableGroupGroupedEvent.class)
    public void handleGroup(final TableGroupGroupedEvent event) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        for (final OrderTable orderTable : orderTables) {
            orderTable.group(event.getTableGroupId());
        }
    }

    @EventListener(TableGroupUngroupedEvent.class)
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
