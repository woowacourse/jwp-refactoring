package kitchenpos.order.dto;

import java.util.List;

public class ValidateAppendOrderTableInTableGroupDto {

    private List<Long> orderTableIds;

    public ValidateAppendOrderTableInTableGroupDto(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
