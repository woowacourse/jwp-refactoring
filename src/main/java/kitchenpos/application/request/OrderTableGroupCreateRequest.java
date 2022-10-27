package kitchenpos.application.request;

public class OrderTableGroupCreateRequest {

    private Long id;

    private OrderTableGroupCreateRequest() {
    }

    public OrderTableGroupCreateRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
