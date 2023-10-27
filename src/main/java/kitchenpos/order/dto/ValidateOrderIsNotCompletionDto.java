package kitchenpos.order.dto;

import java.util.List;

public class ValidateOrderIsNotCompletionDto {

    private List<Long> orderTableIds;

    public ValidateOrderIsNotCompletionDto(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
