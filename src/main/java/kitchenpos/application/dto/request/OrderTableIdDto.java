package kitchenpos.application.dto.request;

import kitchenpos.domain.OrderTable;

public class OrderTableIdDto {

    private Long orderTableId;

    public OrderTableIdDto() {
    }

    public OrderTableIdDto(final OrderTable orderTable) {
        this.orderTableId = orderTable.getId();
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
