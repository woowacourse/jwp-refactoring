package kitchenpos.ui.dto.request;

import java.util.List;

public class CreateTableGroupRequest {

    private List<CreateOrderGroupOrderTableRequest> orderTables;

    public CreateTableGroupRequest() {
    }

    public CreateTableGroupRequest(final List<CreateOrderGroupOrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<CreateOrderGroupOrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
