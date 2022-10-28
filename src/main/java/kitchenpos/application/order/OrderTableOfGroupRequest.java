package kitchenpos.application.order;

public class OrderTableOfGroupRequest {

    private Long id;

    private OrderTableOfGroupRequest() {
    }

    OrderTableOfGroupRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
