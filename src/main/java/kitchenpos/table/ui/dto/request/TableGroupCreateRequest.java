package kitchenpos.table.ui.dto.request;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class TableGroupCreateRequest {

    @NotEmpty
    @Size(min = 2)
    private List<TableCreateDto> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<TableCreateDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableCreateDto> getOrderTables() {
        return orderTables;
    }
}
