package kitchenpos.table.presentation.dto;

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
