package kitchenpos.dto.request;

public class OrderTableIdRequest {

    private final Long id;

    public OrderTableIdRequest(Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }
}
