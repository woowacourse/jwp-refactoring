package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;
import kitchenpos.domain.table.OrderTable;

public class TableGroupCreateWithTableRequest {

    @NotNull
    private Long id;

    private TableGroupCreateWithTableRequest() {
    }

    public TableGroupCreateWithTableRequest(final long id) {
        this.id = id;
    }

    public OrderTable toTable() {
        return new OrderTable(id);
    }

    public long getId() {
        return id;
    }
}
