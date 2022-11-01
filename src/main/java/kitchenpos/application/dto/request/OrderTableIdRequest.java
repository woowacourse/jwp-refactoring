package kitchenpos.application.dto.request;

public class OrderTableIdRequest {

    private Long id;

    private OrderTableIdRequest() {
    }

    public OrderTableIdRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
