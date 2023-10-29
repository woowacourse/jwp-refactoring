package kitchenpos.ordertable.dto;

public class OrderTableEmptyChangeRequest {
    private Boolean empty;

    protected OrderTableEmptyChangeRequest() {
    }

    public OrderTableEmptyChangeRequest(final Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
