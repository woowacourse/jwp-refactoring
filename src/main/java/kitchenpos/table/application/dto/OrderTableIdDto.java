package kitchenpos.table.application.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableIdDto {

    private Long id;

    public OrderTableIdDto() {
    }

    public OrderTableIdDto(final OrderTable orderTable) {
        this.id = orderTable.getId();
    }

    public Long getId() {
        return id;
    }
}
