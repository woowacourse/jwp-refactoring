package kitchenpos.order.application.dto.request.tablegroup;

public class OrderTableIdRequest {

    private Long id;

    public OrderTableIdRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
