package kitchenpos.application.dto.request;

import kitchenpos.domain.OrderTable;

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
