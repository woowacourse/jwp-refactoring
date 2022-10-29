package kitchenpos.dto.request;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private List<OrderTableIdRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final Long... ids) {
        this(toOrderTableIdRequests(ids));
    }

    public TableGroupCreateRequest(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    private static List<OrderTableIdRequest> toOrderTableIdRequests(final Long[] ids) {
        return Arrays.stream(ids)
                .map(OrderTableIdRequest::new)
                .collect(Collectors.toList());
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
