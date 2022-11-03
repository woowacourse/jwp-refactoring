package kitchenpos.ui.jpa.dto.tablegroup;

import java.util.List;
import kitchenpos.domain.entity.OrderTable;

public class TableGroupCreateRequest {

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
