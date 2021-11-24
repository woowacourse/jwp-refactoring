package kitchenpos.application.dto.request;

import java.util.List;

public class TableGroupRequestDto {

    private List<TableIdRequestDto> orderTables;

    private TableGroupRequestDto() {
    }

    public TableGroupRequestDto(
        List<TableIdRequestDto> orderTables
    ) {
        this.orderTables = orderTables;
    }

    public List<TableIdRequestDto> getOrderTables() {
        return orderTables;
    }
}
