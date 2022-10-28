package kitchenpos.ui;

import java.util.List;
import kitchenpos.application.dto.request.OrderTableIdRequestDto;
import org.springframework.util.CollectionUtils;

public class TableGroupCreateRequestDto {

    private final List<OrderTableIdRequestDto> orderTables;

    public TableGroupCreateRequestDto(List<OrderTableIdRequestDto> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private void validate(List<OrderTableIdRequestDto> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTableIdRequestDto> getOrderTables() {
        return orderTables;
    }
}
