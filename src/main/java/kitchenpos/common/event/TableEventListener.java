package kitchenpos.common.event;

import kitchenpos.table.service.TableService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableEventListener {

    private final TableService tableService;

    public TableEventListener(TableService tableService) {
        this.tableService = tableService;
    }

    @EventListener
    public void groupOrderTables(GroupOrderTablesEvent event) {
        tableService.groupOrderTables(event.getTableGroupId(), event.getOrderTableIds());
    }
}
