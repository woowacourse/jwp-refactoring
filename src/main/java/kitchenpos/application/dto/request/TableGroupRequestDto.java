package kitchenpos.application.dto.request;

import java.util.List;

public class TableGroupRequestDto {

    List<Long> orderTableIds;

    private TableGroupRequestDto() {
    }

    public TableGroupRequestDto(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
