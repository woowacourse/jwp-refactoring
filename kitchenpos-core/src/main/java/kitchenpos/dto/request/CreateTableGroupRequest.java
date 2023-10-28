package kitchenpos.dto.request;

import java.util.List;

public class CreateTableGroupRequest {

    private List<OrderTableDto> orderTables;

    public CreateTableGroupRequest() {
    }

    public CreateTableGroupRequest(final List<OrderTableDto> createOrderTableRequests) {
        this.orderTables = createOrderTableRequests;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
