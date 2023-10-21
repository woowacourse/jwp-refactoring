package kitchenpos.application.dto;

import java.util.List;

public class TableGroupCreateDto {

    private final List<Long> orderTableIds;

    public TableGroupCreateDto(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
