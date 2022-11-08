package kitchenpos.tableGroup.ui.request;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;

import kitchenpos.tableGroup.application.request.OrderTableIdRequest;
import kitchenpos.tableGroup.application.request.TableGroupRequest;

public class TableGroupApiRequest {

    private final List<OrderTableIdApiRequest> orderTables;

    @JsonCreator
    public TableGroupApiRequest(List<OrderTableIdApiRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequest toServiceRequest() {
        return new TableGroupRequest(getOrderTableIdRequests());
    }

    public List<OrderTableIdRequest> getOrderTableIdRequests() {
        return orderTables.stream()
                .map(OrderTableIdApiRequest::toServiceRequest)
                .collect(Collectors.toList());
    }
}
