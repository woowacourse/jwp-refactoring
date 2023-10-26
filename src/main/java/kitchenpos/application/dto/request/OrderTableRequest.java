package kitchenpos.application.dto.request;

public class OrderTableRequest {

    private long id;

    public OrderTableRequest() {
    }

    public OrderTableRequest(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
