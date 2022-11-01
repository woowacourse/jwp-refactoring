package kitchenpos.ui.dto.request;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class TableGroupCreateRequest {

    private List<OrderTableIdRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<Long> orderTableIds) {
        this.orderTables = orderTableIds.stream()
                .map(OrderTableIdRequest::new)
                .collect(Collectors.toList());
    }

    public void verify() {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException();
        }
        final long distinctOrderTableIdCount = orderTables.stream()
                .distinct()
                .count();
        if (orderTables.size() != distinctOrderTableIdCount) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
