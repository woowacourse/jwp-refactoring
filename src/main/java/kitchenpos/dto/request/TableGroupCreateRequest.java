package kitchenpos.dto.request;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupCreateRequest {

    private List<TableIdRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<TableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableIdRequest> getOrderTables() {
        return orderTables;
    }

    public TableGroup toEntity(List<OrderTable> orderTables, int actualTablesSize) {
        return TableGroup.create(orderTables, actualTablesSize);
    }
}
