package kitchenpos.table.application.listener;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table_group.application.TableGroupCreateEvent;
import kitchenpos.table_group.application.TableGroupUngroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TableGroupEventListener {

    private final OrderTableRepository orderTableRepository;

    public TableGroupEventListener(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    @Transactional
    public void groupOrderTables(final TableGroupCreateEvent tableGroupCreateEvent) {
        final Long tableGroupId = tableGroupCreateEvent.getTableGroupId();
        orderTableRepository.findAllByIdIn(tableGroupCreateEvent.getTableIds())
            .forEach(orderTable -> orderTable.group(tableGroupId));
    }

    @EventListener
    @Transactional
    public void ungroupTable(final TableGroupUngroupEvent tableGroupUngroupEvent) {
        final Long tableGroupId = tableGroupUngroupEvent.getTableGroupId();
        orderTableRepository.findAllByTableGroupId(tableGroupId)
            .forEach(OrderTable::ungroup);
    }
}
