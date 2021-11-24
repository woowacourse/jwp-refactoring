package kitchenpos.ui.dto.response;

public class OrdersResponse {

    private Long id;

    private OrdersResponse() {
    }

    public OrdersResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
