package kitchenpos.ui.request.tablegroup;

import java.util.List;

public class TableGroupCreatRequest {

    private List<OrderTableDto> orderTables;

    public TableGroupCreatRequest() {
    }

    public TableGroupCreatRequest(List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
