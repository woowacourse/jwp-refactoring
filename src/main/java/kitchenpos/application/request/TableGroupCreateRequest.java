package kitchenpos.application.request;

import java.util.List;

public class TableGroupCreateRequest {

    private final List<OrderTableDto> orderTableDtos;

    public TableGroupCreateRequest(List<OrderTableDto> orderTableDtos) {
        this.orderTableDtos = orderTableDtos;
    }

    public List<OrderTableDto> getOrderTableRequests() {
        return orderTableDtos;
    }
}
