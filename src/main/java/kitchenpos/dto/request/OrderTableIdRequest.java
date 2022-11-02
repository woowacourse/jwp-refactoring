package kitchenpos.dto.request;

public class OrderTableIdRequest {
    private Long id;

    public OrderTableIdRequest() {
    }

    public OrderTableIdRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
