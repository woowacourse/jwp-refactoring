package kitchenpos.order.ui.dto.request;

import java.util.List;

public class TableGroupCreationRequest {

    private List<OrderTableIdDto> orderTables;

    private TableGroupCreationRequest() {
    }

    public TableGroupCreationRequest(final List<OrderTableIdDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdDto> getOrderTables() {
        return orderTables;
    }

    @Override
    public String toString() {
        return "TableGroupCreationRequest{" +
                "orderTables=" + orderTables +
                '}';
    }
}
