package kitchenpos.ui.dto;

public class OrderTableIdRequest {

    private Long id;

    protected OrderTableIdRequest() {
    }

    public OrderTableIdRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}