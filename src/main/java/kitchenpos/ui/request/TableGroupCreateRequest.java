package kitchenpos.ui.request;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private final List<SimpleIdRequest> orderTables;

    public TableGroupCreateRequest(final List<SimpleIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                          .map(SimpleIdRequest::getId)
                          .collect(Collectors.toList());
    }
}
