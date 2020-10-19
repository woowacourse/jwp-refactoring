package kitchenpos.dto;

public class OrderTableResponse {
    private Long id;

    private OrderTableResponse() {
    }

    public OrderTableResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
