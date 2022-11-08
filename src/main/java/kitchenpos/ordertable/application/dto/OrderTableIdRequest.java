package kitchenpos.ordertable.application.dto;

public class OrderTableIdRequest {

    private final Long id;

    public OrderTableIdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
