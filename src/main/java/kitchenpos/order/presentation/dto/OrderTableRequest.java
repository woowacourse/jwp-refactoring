package kitchenpos.order.presentation.dto;

public class OrderTableRequest {

    private Long id;

    private OrderTableRequest() {
    }

    public OrderTableRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
