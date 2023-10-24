package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.IdForRequest;

public class TableGroupCreateRequest {

    private List<IdForRequest> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<IdForRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<IdForRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(IdForRequest::getId)
                .collect(Collectors.toList());
    }
}
