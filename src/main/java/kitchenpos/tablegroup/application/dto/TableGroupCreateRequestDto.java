package kitchenpos.tablegroup.application.dto;

import java.util.List;

public class TableGroupCreateRequestDto {
    private List<Long> orderTableIds;

    private TableGroupCreateRequestDto() {
    }

    public TableGroupCreateRequestDto(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
