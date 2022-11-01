package kitchenpos.menu.ui.request;

import java.util.List;

import kitchenpos.order.ui.request.OrderTableRequest;

public class TableGroupRequest {

    private List<OrderTableRequest> orderTables;

    private TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
