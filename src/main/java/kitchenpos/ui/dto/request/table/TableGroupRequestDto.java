package kitchenpos.ui.dto.request.table;

import java.util.List;

public class TableGroupRequestDto {

    private List<Long> orderTables;


    private TableGroupRequestDto() {
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
