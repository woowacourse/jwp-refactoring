package kitchenpos.dto.request.table;

import java.util.List;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.table.TableIdRequest;

public class CreateTableGroupRequest {
    private List<TableIdRequest> orderTables;

    public CreateTableGroupRequest() {
    }

    public CreateTableGroupRequest(List<TableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableIdRequest> getOrderTables() {
        return orderTables;
    }
}
