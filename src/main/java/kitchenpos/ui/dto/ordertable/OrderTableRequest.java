package kitchenpos.ui.dto.ordertable;

import kitchenpos.domain.OrderTable;

public class OrderTableRequest {

    private Long id;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public OrderTable toEntity() {
        return new OrderTable(this.id);
    }
}
