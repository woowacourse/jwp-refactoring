package kitchenpos.dto.request;

public class OrderTableRequest {

    private final Long id;

    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
