package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;

public class CreateTableGroupRequest {

    private List<CreateTableGroupOrderTableRequest> orderTables;

    public CreateTableGroupRequest() {
    }

    public CreateTableGroupRequest(final List<CreateTableGroupOrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<CreateTableGroupOrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                          .map(CreateTableGroupOrderTableRequest::getId)
                          .distinct()
                          .collect(Collectors.toList());
    }
}
