package kitchenpos.presentation.tablegroup.dto;

import java.util.List;

public class CreateTableGroupRequest {

    private List<Long> orderTableIds;

    public CreateTableGroupRequest() {
    }

    public CreateTableGroupRequest(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public void setOrderTableIds(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }
}
