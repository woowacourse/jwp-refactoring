package kitchenpos.dto.order.request;

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
