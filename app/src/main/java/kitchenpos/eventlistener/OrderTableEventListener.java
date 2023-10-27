package kitchenpos.eventlistener;

import kitchenpos.event.GroupOrderTablesEvent;
import kitchenpos.event.UngroupOrderTablesEvent;
import kitchenpos.event.ValidateOrderTableIsNotEmptyEvent;
import kitchenpos.service.TableService;
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
