package kitchenpos.application.ordertable.dto;

public class OrderTableFindRequest {

    private Long id;

    private OrderTableFindRequest() {
    }

    public OrderTableFindRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
