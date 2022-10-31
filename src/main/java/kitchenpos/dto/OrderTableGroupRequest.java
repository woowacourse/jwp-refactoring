package kitchenpos.dto;

import java.util.List;

public class OrderTableGroupRequest {

    List<Long> tableGroupIds;

    public OrderTableGroupRequest() {
    }

    public OrderTableGroupRequest(List<Long> tableGroupIds) {
        this.tableGroupIds = tableGroupIds;
    }

    public List<Long> getTableGroupIds() {
        return tableGroupIds;
    }
}
