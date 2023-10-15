package kitchenpos.ui.dto;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class TableGroupRequest {
    private final List<OrderTableIdDto> orderTables;

    public TableGroupRequest(List<OrderTableIdDto> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private void validate(final List<OrderTableIdDto> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTableIdDto> getOrderTables() {
        return orderTables;
    }
}
