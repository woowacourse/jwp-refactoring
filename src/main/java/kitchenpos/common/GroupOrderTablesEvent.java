package kitchenpos.common;

import kitchenpos.dto.request.OrderTableIdRequest;

import java.util.List;

public class GroupOrderTablesEvent {

    private final List<OrderTableIdRequest> orderTableIdRequests;
    private final Long tableGroupId;

    public GroupOrderTablesEvent(final List<OrderTableIdRequest> orderTableIdRequests, final Long tableGroupId) {
        this.orderTableIdRequests = orderTableIdRequests;
        this.tableGroupId = tableGroupId;
    }

    public List<OrderTableIdRequest> getOrderTableIdRequests() {
        return orderTableIdRequests;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
