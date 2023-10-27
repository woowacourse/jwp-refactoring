package kitchenpos.order.dto;

import java.util.List;

public class ValidateSameSizeOrderTableDto {

    private List<Long> orderTableIds;

    public ValidateSameSizeOrderTableDto(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
