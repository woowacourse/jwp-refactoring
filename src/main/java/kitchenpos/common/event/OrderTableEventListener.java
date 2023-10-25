package kitchenpos.common.event;

import kitchenpos.table.service.TableService;
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
}
