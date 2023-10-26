package kitchenpos.application.dto.request;

public class OrderTableIdRequest {

    private long id;

    public OrderTableIdRequest(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
