package kitchenpos.ui.dto.tablegroup;

import kitchenpos.ui.dto.order.OrderTableIdRequest;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {

    @NotNull
    private List<OrderTableIdRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds(){
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }
}
