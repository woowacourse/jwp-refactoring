package kitchenpos.ordertable.dto;

public class OrderTableEmptyChangeRequest {

    private Boolean empty;

    private OrderTableEmptyChangeRequest() {
    }

    public OrderTableEmptyChangeRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean isEmpty() {
        return empty;
    }
}
