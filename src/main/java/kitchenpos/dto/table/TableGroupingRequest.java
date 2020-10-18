package kitchenpos.dto.table;

import java.util.List;

public class TableGroupingRequest {
    private List<OrderTableResponse> orderTableDtos;

    public TableGroupingRequest() {
    }

    public TableGroupingRequest(List<OrderTableResponse> orderTableDtos) {
        this.orderTableDtos = orderTableDtos;
    }

    public List<OrderTableResponse> getOrderTableDtos() {
        return orderTableDtos;
    }
}
