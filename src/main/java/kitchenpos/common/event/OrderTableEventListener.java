package kitchenpos.common.event;

import kitchenpos.order.event.ValidateOrderTableIsNotEmptyEvent;
import kitchenpos.table.service.TableService;
import kitchenpos.tablegroup.event.GroupOrderTablesEvent;
import kitchenpos.tablegroup.event.UngroupOrderTablesEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderTableEventListener {

    private final TableService tableService;

    public OrderTableEventListener(TableService tableService) {
        this.tableService = tableService;
    }

    @EventListener
    public void ungroupOrderTables(UngroupOrderTablesEvent event) {
        tableService.ungroupByTableGroup(event.getTableGroupId());
    }

    @EventListener
    public void validateOrderTableIsNotEmpty(ValidateOrderTableIsNotEmptyEvent event) {
        tableService.validateNotEmpty(event.getOrderTableId());
    }

    @EventListener
    public void groupOrderTables(GroupOrderTablesEvent event) {
        tableService.groupOrderTables(event.getTableGroupId(), event.getOrderTableIds());
    }
}
