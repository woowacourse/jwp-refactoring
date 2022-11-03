package kitchenpos.order.ui.request;

public class OrderTableGroupRequest {

    private Long id;

    private OrderTableGroupRequest() {
    }

    public OrderTableGroupRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
