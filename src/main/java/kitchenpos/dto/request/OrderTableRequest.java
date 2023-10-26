package kitchenpos.dto.request;

public class OrderTableRequest {

    private final Long id;

    public OrderTableRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
