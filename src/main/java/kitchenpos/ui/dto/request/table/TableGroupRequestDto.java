package kitchenpos.ui.dto.request.table;

import java.util.List;

public class TableGroupRequestDto {

    private List<OrderTableDto> orderTables;


    private TableGroupRequestDto() {
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
