package kitchenpos.application.dto.request;

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
