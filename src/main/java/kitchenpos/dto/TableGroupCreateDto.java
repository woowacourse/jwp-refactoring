package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;

public class TableGroupCreateDto {

    private final List<TableIdDto> orderTables;

    @JsonCreator
    public TableGroupCreateDto(final List<TableIdDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableIdDto> getOrderTables() {
        return orderTables;
    }
}
