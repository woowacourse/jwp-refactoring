package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;

public class CraeteTableGroupRequest {

    private List<OrderTableIdRequest> orderTables;

    protected CraeteTableGroupRequest() {
    }

    public CraeteTableGroupRequest(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }
}
