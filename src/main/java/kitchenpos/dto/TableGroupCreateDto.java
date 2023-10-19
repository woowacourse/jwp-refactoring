package kitchenpos.dto;

import java.util.List;

public class TableGroupCreateDto {

    private List<TableIdDto> orderTables;

    public TableGroupCreateDto(final List<TableIdDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableIdDto> getOrderTables() {
        return orderTables;
    }
}
