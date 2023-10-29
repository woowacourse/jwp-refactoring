package kitchenpos.table.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.table.application.dto.OrderTableDto;

import java.util.List;

public class TableGroupCreateRequest {

    private final List<OrderTableDto> orderTables;

    public TableGroupCreateRequest(@JsonProperty(value = "orderTables") List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
