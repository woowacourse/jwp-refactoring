package kitchenpos.application.dto.request.tablegroup;

import java.util.List;

public class TableGroupRequestDto {

    private List<OrderTableGroupRequestDto> orderTables;

    private TableGroupRequestDto() {
    }

    public TableGroupRequestDto(List<OrderTableGroupRequestDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableGroupRequestDto> getOrderTables() {
        return orderTables;
    }
}
