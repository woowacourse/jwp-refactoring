package kitchenpos.dto.table;

import java.util.List;

public class TableGroupRequest {
    private List<OrderTableResponse> orderTableDtos;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableResponse> orderTableDtos) {
        this.orderTableDtos = orderTableDtos;
    }

    public List<OrderTableResponse> getOrderTableDtos() {
        return orderTableDtos;
    }
}
