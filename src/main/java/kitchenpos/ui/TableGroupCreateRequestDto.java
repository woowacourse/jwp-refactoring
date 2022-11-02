package kitchenpos.ui;

import java.util.List;
import kitchenpos.application.dto.request.OrderTableIdRequest;
import org.springframework.util.CollectionUtils;

public class TableGroupCreateRequestDto {

    private final List<OrderTableIdRequest> orderTables;

    public TableGroupCreateRequestDto(List<OrderTableIdRequest> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private void validate(List<OrderTableIdRequest> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
