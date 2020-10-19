package kitchenpos.dto;

public class OrderResponse {
    private Long id;

    private OrderResponse() {
    }

    public OrderResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
