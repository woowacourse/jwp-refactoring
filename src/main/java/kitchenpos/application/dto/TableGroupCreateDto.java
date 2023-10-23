package kitchenpos.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;

public class TableGroupCreateDto {

    private final List<Long> orderTableIds;

    @JsonCreator
    public TableGroupCreateDto(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
