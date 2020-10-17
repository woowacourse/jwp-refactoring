package kitchenpos.dto.tablegroup;

import java.util.List;

public class TableGroupingRequest {
    private List<OrderTableDto> orderTableDtos;

    public TableGroupingRequest() {
    }

    public TableGroupingRequest(List<OrderTableDto> orderTableDtos) {
        this.orderTableDtos = orderTableDtos;
    }

    public List<OrderTableDto> getOrderTableDtos() {
        return orderTableDtos;
    }
}
