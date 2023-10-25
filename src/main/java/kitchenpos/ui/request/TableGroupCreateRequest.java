package kitchenpos.ui.request;

import java.util.List;
import javax.validation.constraints.NotNull;

public class TableGroupCreateRequest {

    @NotNull
    private List<Long> orderTableIds;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

}
