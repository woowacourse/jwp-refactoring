package kitchenpos.dto.request;

import java.util.List;

public class TableGroupCreateRequest {

    private List<Long> orderTableId;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<Long> orderTableId) {
        this.orderTableId = orderTableId;
    }

    public List<Long> getOrderTableIds() {
        return orderTableId;
    }

}
