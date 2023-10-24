package kitchenpos.ordertable.application.dto;

public class OrderTableFindRequest {

    private final Long id;

    public OrderTableFindRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
