package kitchenpos.dto.table;

import javax.validation.constraints.NotEmpty;

import java.util.List;

public class TableGroupRequest {
    @NotEmpty
    private List<OrderTableDto> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
