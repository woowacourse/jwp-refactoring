package kitchenpos.order.ui.dto;

import javax.validation.constraints.NotNull;
import kitchenpos.order.domain.OrderTable;

public class TableGroupCreateWithTableRequest {

    @NotNull
    private Long id;

    private TableGroupCreateWithTableRequest() {
    }

    public TableGroupCreateWithTableRequest(long id) {
        this.id = id;
    }

    public OrderTable toTable() {
        return new OrderTable(id);
    }

    public long getId() {
        return id;
    }
}
