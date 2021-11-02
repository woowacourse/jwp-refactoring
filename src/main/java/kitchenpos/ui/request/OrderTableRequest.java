package kitchenpos.ui.request;

import kitchenpos.domain.OrderTable;

public class OrderTableRequest {

    private Long id;

    public OrderTableRequest() {
    }

    public OrderTableRequest(final Long id) {
        this.id = id;
    }

    public OrderTable toEntity() {
        return new OrderTable(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
